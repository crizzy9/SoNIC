package com.example.android.materialdesigncodelab;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

public class LazyImageLoadAdapter extends BaseAdapter implements OnClickListener {

	private Activity activity;
    public String uname;
	private ArrayList<ListItem> data;
	public String post;
	private static LayoutInflater inflater = null;
	public ImageLoader imageLoader;
    public String uploadURL=Links.commonUrl+"/scripts/upvote.php";
	
	public LazyImageLoadAdapter(Activity a, ArrayList<ListItem> d, String username) {
		activity = a; 
		data = d;
        uname=username;
		
		inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		imageLoader = new ImageLoader(activity.getApplicationContext());
	}
	
	@Override
	public int getCount() {
		return data.size();
	}

	@Override
	public Object getItem(int pos) {
		return pos;
	}

	@Override
	public long getItemId(int pos) {
		return pos;
	}

	public static class ViewHolder {
		public TextView headlineView;
		public TextView reporterNameView;
		public TextView reportedDateView;
		public TextView upvotes;
        public Button dvt;
		public ImageButton upvt;
		public String postid;
		public ImageView imageView;
	}
	
	@Override
	public View getView(final int pos, View convertView, ViewGroup parent) {
		
		View vi = convertView;
		final ViewHolder holder;
		
		if(convertView == null) {
			
			vi = inflater.inflate(R.layout.item_card, null);
			
			holder = new ViewHolder();
			
			//holder.text = (TextView) vi.findViewById(R.id.text);
			//holder.text1 = (TextView) vi.findViewById(R.id.text1);
			//holder.image = (ImageView) vi.findViewById(R.id.image);
			holder.headlineView = (TextView) vi.findViewById(R.id.card_text);
			//holder.reporterNameView = (TextView) vi.findViewById(R.id.reporter);
			//holder.reportedDateView = (TextView) vi.findViewById(R.id.date);
			//holder.upvotes = (TextView) vi.findViewById(R.id.textView3);
			holder.upvt = (ImageButton) vi.findViewById(R.id.favorite_button);
            //holder.dvt = (Button) vi.findViewById(R.id.Downvote);
			holder.imageView = (ImageView) vi.findViewById(R.id.card_image);

            //dont use setTags position lags
            //holder.upvt.setTag(pos);
			
			vi.setTag(holder);
			
		} else {
			holder = (ViewHolder) vi.getTag();
		}

        //holder.text.setText("Company " + pos);
		//holder.text1.setText("company description " + pos);
		ImageView image = holder.imageView;

		ListItem newsItem = data.get(pos);
		holder.postid =newsItem.getPostid();
		post = holder.postid;
		holder.headlineView.setText(newsItem.getHeadline());
		//holder.reporterNameView.setText("By, " + newsItem.getReporterName());
		//holder.reportedDateView.setText(newsItem.getDate());
        //System.out.println(Integer.parseInt(newsItem.getUpvotes())-Integer.parseInt(newsItem.getDownvotes()));
        //int cnt = Integer.parseInt(newsItem.getUpvotes())-Integer.parseInt(newsItem.getDownvotes());
        //System.out.println("count is : " + cnt);
		//holder.upvotes.setText("upvotes: "+ newsItem.getUpvotes());

        System.out.println("Inside Adapter:");
        System.out.println("Image: "+pos+"   "+holder.postid+"   "+newsItem.getHeadline());

		imageLoader.displayImage(newsItem.getUrl(), image);



        //x=holder.toString();
        System.out.println(holder.toString());
        holder.upvt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Toast.makeText(ListDisplay.class,"you presssed:",Toast.LENGTH_LONG).show();
                System.out.println(" :: Upvote to :" + pos + "    " + holder.postid + "  " + uname);
                /*data = new HashMap<String, String>();
                data.put("postid",post);
                data.put("username", uname);
                sendData();*/
                holder.upvt.setEnabled(false);
                register(holder.postid, uname);
                /*RegisterUserClass ruc = new RegisterUserClass();
                ruc.sendPostRequest(uploadURL, data);*/
            }
        });

        /*holder.dvt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Downvote to: " + pos + "  "+holder.postid+"  "+uname);
                holder.dvt.setEnabled(false);
            }
        });
*/
        //holder.upvt.setOnClickListener(new OnItemClickListener(pos));
		
		return vi;
	}

    private void register(String postid, String username) {
        class RegisterUser extends AsyncTask<String, Void, String> {
            //ProgressDialog loading;
            RegisterUserClass ruc = new RegisterUserClass();


            /*@Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(MainActivity.this, "Please Wait",null, true, true);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                Toast.makeText(getApplicationContext(),s,Toast.LENGTH_LONG).show();
            }
*/
            @Override
            protected String doInBackground(String... params) {
                System.out.println("in background");
                HashMap<String, String> data = new HashMap<String,String>();
                data.put("postid",params[0]);
                data.put("username",params[1]);

                String result = ruc.sendPostRequest(uploadURL,data);

                return  result;
            }
        }

        RegisterUser ru = new RegisterUser();
        ru.execute(postid,username);
    }




	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
	}
	
	/*private class OnItemClickListener implements OnClickListener {

		private int position;
		
		OnItemClickListener(int pos) {
			position = pos;
		}
		
		@Override
		public void onClick(View v) {
			*//*MainActivity sct = (MainActivity) activity;
			sct.onItemClick(position);*//*

            int position = (Integer) v.getTag();
            //Toast.makeText(ListDisplay.class,"you presssed:",Toast.LENGTH_LONG).show();
            System.out.println(" :: Upvote to :" + position + "    " + post + "  " + uname);
            *//*data = new HashMap<String, String>();
            data.put("postid",post);
            data.put("username", uname);
            sendData();*//*
            register(post, uname);
		}
	}*/
}
