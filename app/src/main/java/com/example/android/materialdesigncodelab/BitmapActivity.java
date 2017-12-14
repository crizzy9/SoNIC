//Activity Needed-Alex


package com.example.android.materialdesigncodelab;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.util.LruCache;
import android.support.v4.widget.CursorAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.ref.WeakReference;

public class BitmapActivity extends Activity  {
	
	private Button btnClickImage, btnFromGallery, btnSend, btnCancel;
	private ListView listView;
	private ImageView imgView;
	
	private ImageUriDatabase database;
	private ImageListAdapter adapter;
	private ImageLoadingUtils utils;
	
	private Cursor cursor;
	private LruCache<String, Bitmap> memoryCache;
	
	private final int REQUEST_CODE_FROM_GALLERY = 01;
	private final int REQUEST_CODE_CLICK_IMAGE = 02;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bitmap);
		
		database = new ImageUriDatabase(this);
		utils = new ImageLoadingUtils(this);
		int cachesize = 60*1024*1024;






		memoryCache	 = new LruCache<String, Bitmap>(cachesize){
			@SuppressLint("NewApi")
			@Override
			protected int sizeOf(String key, Bitmap value) {
				 if(android.os.Build.VERSION.SDK_INT>=12){
					 return value.getByteCount();
				 }
				 else{
					 return value.getRowBytes()*value.getHeight();
				 }
			}
		};
		initViews();
		
//		cursor = database.getallUri();
//		adapter = new ImageListAdapter(this, cursor, true);
//		listView.setAdapter(adapter);
		selectImage();


//		Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//		intent.setType("image/*");
//		startActivityForResult(intent, REQUEST_CODE_FROM_GALLERY);
	}


	private void selectImage() {
		final CharSequence[] items = { "Take Photo", "Choose from Library",
				"Cancel" };

		AlertDialog.Builder builder = new AlertDialog.Builder(BitmapActivity.this);
		builder.setCancelable(false);
		builder.setTitle("Add Photo!");
		builder.setItems(items, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int item) {
				if (items[item].equals("Take Photo")) {
					Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
					startActivityForResult(intent, REQUEST_CODE_CLICK_IMAGE);
				} else if (items[item].equals("Choose from Library")) {
					Intent intent = new Intent(
							Intent.ACTION_PICK,
							android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
					intent.setType("image/*");
					startActivityForResult(
							Intent.createChooser(intent, "Select File"),
							REQUEST_CODE_FROM_GALLERY);
				} else if (items[item].equals("Cancel")) {
					Intent ix = new Intent(getApplicationContext(), MainActivity.class);
					startActivity(ix);
					dialog.dismiss();

				}
			}
		});
		builder.show();
	}




	private void initViews() {
//		 btnClickImage = (Button) findViewById(R.id.btnClickImage);
//		 btnFromGallery = (Button) findViewById(R.id.btnFromGallery);
//		 listView = (ListView) findViewById(R.id.listView);
//
//		 btnClickImage.setOnClickListener(this);
//		 btnFromGallery.setOnClickListener(this);
//
	}
	
	public void addBitmapToMemoryCache(String key, Bitmap bitmap) {
	    if (getBitmapFromMemCache(key) == null) {
	    	memoryCache.put(key, bitmap);
	    }
	}

	public Bitmap getBitmapFromMemCache(String key) {
	    return memoryCache.get(key);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		//getMenuInflater().inflate(R.menu.bitmap, menu);
		return true;
	}

	//@Override
//	public void onClick(View v) {
//		 switch(v.getId()){
//		 case R.id.btnClickImage:
//		 {
//			 Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//			 startActivityForResult(intent, REQUEST_CODE_CLICK_IMAGE);
//		 }
//		 break;
//		 case R.id.btnFromGallery :
//		 {
//			 Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//			 intent.setType("image/*");
//			 startActivityForResult(intent, REQUEST_CODE_FROM_GALLERY);
//		 }
//		 break;
//		 }
//
//	}
//
	@Override
	@Deprecated
	protected Dialog onCreateDialog(int id) {
		Dialog dialog = new Dialog(this);
		dialog.setContentView(R.layout.dialog_layout);
		imgView = (ImageView) dialog.findViewById(R.id.dlgImageView);
		btnCancel = (Button) dialog.findViewById(R.id.btnCancel);
		btnSend = (Button) dialog.findViewById(R.id.btnSend);
		return dialog;
	}
	
	@Override
	@Deprecated
	protected void onPrepareDialog(int id, final Dialog dialog, Bundle bundle) {
		switch (id){
		case 1:
			if(bundle != null){
				final String filePath = bundle.getString("FILE_PATH");
				imgView.setImageBitmap(utils.decodeBitmapFromPath(filePath));
				
				btnCancel.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						dialog.dismiss();
						
					}
				});
				
				btnSend.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						database.insertUri(filePath);
						cursor = database.getallUri();
						adapter.changeCursor(cursor);	
						dialog.dismiss();
					}
				});
			}
		}
	}
	
	@Override
	protected void onDestroy() {
		memoryCache.evictAll();
		super.onDestroy();
	}
	private String getRealPathFromURI(Uri contentUri) {
		String[] proj = { MediaStore.Images.Media.DATA };
		CursorLoader loader = new CursorLoader(getApplicationContext(), contentUri, proj, null, null, null);
		Cursor cursor = loader.loadInBackground();
		int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
		cursor.moveToFirst();
		String result = cursor.getString(column_index);
		cursor.close();
		return result;
	}


	public String compressImage(String imageUri) {

		String filePath = imageUri;
		Bitmap scaledBitmap = null;
		Toast.makeText(getApplicationContext(),"AAAAAAAAAAAAAAAAA: Image:"+filePath,Toast.LENGTH_LONG).show();
		System.out.println(" Image:"+filePath);

		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		Bitmap bmp = BitmapFactory.decodeFile(filePath, options);

		int actualHeight = options.outHeight;
		int actualWidth = options.outWidth;
		float maxHeight = 816.0f;
		float maxWidth = 612.0f;
		float imgRatio = actualWidth / actualHeight;
		float maxRatio = maxWidth / maxHeight;

		if (actualHeight > maxHeight || actualWidth > maxWidth) {
			if (imgRatio < maxRatio) {
				imgRatio = maxHeight / actualHeight;
				actualWidth = (int) (imgRatio * actualWidth);
				actualHeight = (int) maxHeight;
			} else if (imgRatio > maxRatio) {
				imgRatio = maxWidth / actualWidth;
				actualHeight = (int) (imgRatio * actualHeight);
				actualWidth = (int) maxWidth;
			} else {
				actualHeight = (int) maxHeight;
				actualWidth = (int) maxWidth;

			}
		}

		options.inSampleSize = utils.calculateInSampleSize(options, actualWidth, actualHeight);
		options.inJustDecodeBounds = false;
		options.inDither = false;
		options.inPurgeable = true;
		options.inInputShareable = true;
		options.inTempStorage = new byte[16*1024];

		try{
			bmp = BitmapFactory.decodeFile(filePath, options);
		}
		catch(OutOfMemoryError exception){
			exception.printStackTrace();

		}
		try{
			scaledBitmap = Bitmap.createBitmap(actualWidth, actualHeight, Bitmap.Config.ARGB_8888);
		}
		catch(OutOfMemoryError exception){
			exception.printStackTrace();
		}

		float ratioX = actualWidth / (float) options.outWidth;
		float ratioY = actualHeight / (float)options.outHeight;
		float middleX = actualWidth / 2.0f;
		float middleY = actualHeight / 2.0f;

		Matrix scaleMatrix = new Matrix();
		scaleMatrix.setScale(ratioX, ratioY, middleX, middleY);

		Canvas canvas = new Canvas(scaledBitmap);
		canvas.setMatrix(scaleMatrix);
		canvas.drawBitmap(bmp, middleX - bmp.getWidth()/2, middleY - bmp.getHeight() / 2, new Paint(Paint.FILTER_BITMAP_FLAG));


		ExifInterface exif;
		try {
			exif = new ExifInterface(filePath);

			int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 0);
			Log.d("EXIF", "Exif: " + orientation);
			Matrix matrix = new Matrix();
			if (orientation == 6) {
				matrix.postRotate(90);
				Log.d("EXIF", "Exif: " + orientation);
			} else if (orientation == 3) {
				matrix.postRotate(180);
				Log.d("EXIF", "Exif: " + orientation);
			} else if (orientation == 8) {
				matrix.postRotate(270);
				Log.d("EXIF", "Exif: " + orientation);
			}
			scaledBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0, scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix, true);
		} catch (IOException e) {
			e.printStackTrace();
		}
		Toast.makeText(getApplicationContext(),"Image Compressessed",Toast.LENGTH_LONG).show();
		System.out.println("AAAAAAAAAAAAAAAAAAAAAAAAAAAAA: Image Compressessed");
		FileOutputStream out = null;
		String filename = getFilename();
		try {
			out = new FileOutputStream(filename);
			scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		Toast.makeText(getApplicationContext(),"AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA: Final image name : "+filename,Toast.LENGTH_LONG).show();
		System.out.println("AAAAAAAAAAAAAAAAAAAAAAAAAAAAA: Final image name : "+filename);

		return filename;

	}
	public String getFilename() {
		File file = new File(Environment.getExternalStorageDirectory().getPath(), "MyFolder/Images");
		if (!file.exists()) {
			file.mkdirs();
		}
		String uriSting = (file.getAbsolutePath() + "/"+ System.currentTimeMillis() + ".jpg");
		return uriSting;

	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode == RESULT_OK) {
			switch (requestCode) {
				case REQUEST_CODE_FROM_GALLERY:
					String xx = getRealPathFromURI(data.getData());
					System.out.println("FIIIILLLLLEEEE: " + xx);
					String finalImageName = compressImage(xx);
					Intent intent = new Intent(this, UploadToServer.class);
					intent.putExtra("finalImage", finalImageName);
					startActivity(intent);
//				new ImageCompressionAsyncTask(true).execute(data.getDataString());

					break;
				case REQUEST_CODE_CLICK_IMAGE:


					Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
					Uri tempUri = getImageUri(getApplicationContext(), thumbnail);

					// CALL THIS METHOD TO GET THE ACTUAL PATH
					String xy = getRealPathFromURI(tempUri);
					System.out.println("XYYYYYYYYY: " + xy);
					String finalsImageName = compressImage(xy);
					Intent intent1 = new Intent(this, UploadToServer.class);
					intent1.putExtra("finalImage", finalsImageName);
					startActivity(intent1);


					//new ImageCompressionAsyncTask(false).execute(data.getDataString());
					break;


			}
		}else
		{	Intent ix = new Intent(this, MainActivity.class);
			startActivity(ix);}
	}
	public Uri getImageUri(Context inContext, Bitmap inImage) {
		ByteArrayOutputStream bytes = new ByteArrayOutputStream();
		inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
		String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
		return Uri.parse(path);
	}

	public void onBackPressed() {
		// Do Here what ever you want do on back press;

	}



	class ImageCompressionAsyncTask extends AsyncTask<String, Void, String> {
		private boolean fromGallery;
		
		public ImageCompressionAsyncTask(boolean fromGallery){
			this.fromGallery = fromGallery;
		}

		@Override
		protected String doInBackground(String... params) {
			String filePath = compressImage(params[0]);
			return filePath;
		}
		
		public String compressImage(String imageUri) {
			
			String filePath = getRealPathFromURI(imageUri);
			Bitmap scaledBitmap = null;
			
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inJustDecodeBounds = true;						
			Bitmap bmp = BitmapFactory.decodeFile(filePath, options);
			
			int actualHeight = options.outHeight;
			int actualWidth = options.outWidth;
			float maxHeight = 408.0f;
			float maxWidth = 306.0f;
			float imgRatio = actualWidth / actualHeight;
			float maxRatio = maxWidth / maxHeight;

			if (actualHeight > maxHeight || actualWidth > maxWidth) {
				if (imgRatio < maxRatio) {
					imgRatio = maxHeight / actualHeight;
					actualWidth = (int) (imgRatio * actualWidth);
					actualHeight = (int) maxHeight;
				} else if (imgRatio > maxRatio) {
					imgRatio = maxWidth / actualWidth;
					actualHeight = (int) (imgRatio * actualHeight);
					actualWidth = (int) maxWidth;
				} else {
					actualHeight = (int) maxHeight;
					actualWidth = (int) maxWidth;     
					
				}
			}
					
			options.inSampleSize = utils.calculateInSampleSize(options, actualWidth, actualHeight);
			options.inJustDecodeBounds = false;
			options.inDither = false;
			options.inPurgeable = true;
			options.inInputShareable = true;
			options.inTempStorage = new byte[16*1024];
				
			try{	
				bmp = BitmapFactory.decodeFile(filePath, options);
			}
			catch(OutOfMemoryError exception){
				exception.printStackTrace();
				
			}
			try{
				scaledBitmap = Bitmap.createBitmap(actualWidth, actualHeight, Bitmap.Config.ARGB_8888);
			}
			catch(OutOfMemoryError exception){
				exception.printStackTrace();
			}
							
			float ratioX = actualWidth / (float) options.outWidth;
			float ratioY = actualHeight / (float)options.outHeight;
			float middleX = actualWidth / 2.0f;
			float middleY = actualHeight / 2.0f;
				
			Matrix scaleMatrix = new Matrix();
			scaleMatrix.setScale(ratioX, ratioY, middleX, middleY);

			Canvas canvas = new Canvas(scaledBitmap);
			canvas.setMatrix(scaleMatrix);
			canvas.drawBitmap(bmp, middleX - bmp.getWidth()/2, middleY - bmp.getHeight() / 2, new Paint(Paint.FILTER_BITMAP_FLAG));

							
			ExifInterface exif;
			try {
				exif = new ExifInterface(filePath);
			
				int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 0);
				Log.d("EXIF", "Exif: " + orientation);
				Matrix matrix = new Matrix();
				if (orientation == 6) {
					matrix.postRotate(90);
					Log.d("EXIF", "Exif: " + orientation);
				} else if (orientation == 3) {
					matrix.postRotate(180);
					Log.d("EXIF", "Exif: " + orientation);
				} else if (orientation == 8) {
					matrix.postRotate(270);
					Log.d("EXIF", "Exif: " + orientation);
				}
				scaledBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0, scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix, true);
			} catch (IOException e) {
				e.printStackTrace();
			}
			FileOutputStream out = null;
			String filename = getFilename();
			try {
				out = new FileOutputStream(filename);
				scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 80, out);
				
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			
			return filename;

		}
		
		private String getRealPathFromURI(String contentURI) {
			Uri contentUri = Uri.parse(contentURI);
			Cursor cursor = getContentResolver().query(contentUri, null, null, null, null);
			if (cursor == null) {
				return contentUri.getPath();
			} else {
				cursor.moveToFirst();
				int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
				return cursor.getString(idx);
			}
		}
		
		public String getFilename() {
			File file = new File(Environment.getExternalStorageDirectory().getPath(), "MyFolder/Images");
			if (!file.exists()) {
				file.mkdirs();
			}
			String uriSting = (file.getAbsolutePath() + "/"+ System.currentTimeMillis() + ".jpg");
			return uriSting;

		}
		


		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			if(fromGallery){
				Bundle bundle = new Bundle();
				bundle.putString("FILE_PATH", result);
				showDialog(1, bundle);
			}
			else{
				database.insertUri(result);
				cursor = database.getallUri();
				adapter.changeCursor(cursor);
			}
		}
		
	}

	class ImageListAdapter extends CursorAdapter {
		private ImageLoadingUtils utils;

		public ImageListAdapter(Context context, Cursor cursor, boolean autoRequery) {
			super(context, cursor, autoRequery);
			utils = new ImageLoadingUtils(context);				
		}

		@Override
		public void bindView(View view, Context context, Cursor cursor) {
			ViewHolder holder = (ViewHolder) view.getTag();
			loadBitmap(cursor.getString(cursor.getColumnIndex(ImageUriDatabase.PATH_NAME)), holder.imageView, context);
			
		}

		@Override
		public View newView(Context context, Cursor cursor, ViewGroup parent) {
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View view = inflater.inflate(R.layout.list_item_layout, parent, false);
			ViewHolder holder = new ViewHolder();
			holder.imageView = (ImageView) view.findViewById(R.id.imgView);
			view.setTag(holder);
			return view;
		}
		
		public void loadBitmap(String filePath, ImageView imageView, Context context) {
			if (cancelPotentialWork(filePath, imageView)) {
				final Bitmap bitmap = getBitmapFromMemCache(filePath);
				if(bitmap != null){
					imageView.setImageBitmap(bitmap);
				}
				else{
			        final BitmapWorkerTask task = new BitmapWorkerTask(imageView);
			        final AsyncDrawable asyncDrawable = new AsyncDrawable(context.getResources(), utils.icon, task);
			        imageView.setImageDrawable(asyncDrawable);
			        task.execute(filePath);
				}
		    }
		}
		
		class ViewHolder{
			ImageView imageView;
		}
		
		class AsyncDrawable extends BitmapDrawable {
			
		    private final WeakReference<BitmapWorkerTask> bitmapWorkerTaskReference;

		    public AsyncDrawable(Resources res, Bitmap bitmap, BitmapWorkerTask bitmapWorkerTask) {
		        super(res, bitmap);
		        bitmapWorkerTaskReference = new WeakReference<BitmapWorkerTask>(bitmapWorkerTask);
		    }

		    public BitmapWorkerTask getBitmapWorkerTask() {
		        return bitmapWorkerTaskReference.get();
		    }
		}
		
		public boolean cancelPotentialWork(String filePath, ImageView imageView) {
			
		    final BitmapWorkerTask bitmapWorkerTask = getBitmapWorkerTask(imageView);
		    
		    if (bitmapWorkerTask != null) {
		        final String bitmapFilePath = bitmapWorkerTask.filePath;
		        if (bitmapFilePath != null && !bitmapFilePath.equalsIgnoreCase(filePath)) {
		            bitmapWorkerTask.cancel(true);
		        } else {
		            return false;
		        }
		    }
		    return true;
		}
		
		private BitmapWorkerTask getBitmapWorkerTask(ImageView imageView) {
			   if (imageView != null) {
			       final Drawable drawable = imageView.getDrawable();
			       if (drawable instanceof AsyncDrawable) {
			           final AsyncDrawable asyncDrawable = (AsyncDrawable) drawable;
			           return asyncDrawable.getBitmapWorkerTask();
			       }
			    }
			    return null;
			}
		
		class BitmapWorkerTask extends AsyncTask<String, Void, Bitmap> {
			private final WeakReference<ImageView> imageViewReference;
			public String filePath;
			
			public BitmapWorkerTask(ImageView imageView){
				imageViewReference = new WeakReference<ImageView>(imageView);
			}

			@Override
			protected Bitmap doInBackground(String... params) {
				filePath = params[0];
				Bitmap bitmap = utils.decodeBitmapFromPath(filePath);
				addBitmapToMemoryCache(filePath, bitmap);
				return bitmap;
			}
			
			@Override
			protected void onPostExecute(Bitmap bitmap) {
				if (isCancelled()) {
		            bitmap = null;
		        }
				if(imageViewReference != null && bitmap != null){
					final ImageView imageView = imageViewReference.get();
					final BitmapWorkerTask bitmapWorkerTask = getBitmapWorkerTask(imageView);
		            if (this == bitmapWorkerTask && imageView != null) {
		                imageView.setImageBitmap(bitmap);
		            }
				}
			}
		}

	}


}
