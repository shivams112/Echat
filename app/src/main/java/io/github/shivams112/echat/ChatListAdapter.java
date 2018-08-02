package io.github.shivams112.echat;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class ChatListAdapter extends BaseAdapter {

    private Activity mActivity;
    private DatabaseReference mDatabaseReference;
    private String mDisplayName;
    private ArrayList<DataSnapshot> mDataSnapshots;



    ChildEventListener mListener = new ChildEventListener() {
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                mDataSnapshots.add(dataSnapshot);
                notifyDataSetChanged();
        }

        @Override
        public void onChildChanged(DataSnapshot dataSnapshot, String s) {

        }

        @Override
        public void onChildRemoved(DataSnapshot dataSnapshot) {

        }

        @Override
        public void onChildMoved(DataSnapshot dataSnapshot, String s) {

        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };

    public ChatListAdapter(Activity activity , DatabaseReference databaseReference ,String name){

        mActivity = activity;
        mDatabaseReference = databaseReference.child("message");
        mDatabaseReference.addChildEventListener(mListener);
        mDisplayName = name;
        mDataSnapshots = new ArrayList<>();
    }

    static  class  ViewHolder{

        TextView authorName;
        TextView body;
        LinearLayout.LayoutParams params;
    }

    @Override
    public int getCount() {
        return mDataSnapshots.size();
    }

    @Override
    public InstantMessage getItem(int position) {

        DataSnapshot dataSnapshot = mDataSnapshots.get(position);
        Log.d("Echat" ,"Message: "+ dataSnapshot.getValue(InstantMessage.class).toString());
        return dataSnapshot.getValue(InstantMessage.class);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView==null){
            LayoutInflater inflater =(LayoutInflater) mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE );
            convertView = inflater.inflate(R.layout.chat_msg_row ,parent ,false);

            final ViewHolder viewHolder = new ViewHolder();
            viewHolder.authorName =  convertView.findViewById(R.id.author);
            viewHolder.body = convertView.findViewById(R.id.message);
            viewHolder.params = (LinearLayout.LayoutParams) viewHolder.authorName.getLayoutParams();
            convertView.setTag(viewHolder);

        }



        final  InstantMessage instantMessage = getItem(position);
        final ViewHolder holder = (ViewHolder) convertView.getTag();

         try{
       boolean  isItMe = instantMessage.getUser().equals(mDisplayName);
             if(isItMe){
                 holder.params.gravity = Gravity.END;
                 holder.params.leftMargin = 150;
                 holder.params.rightMargin =0;
                 holder.authorName.setTextColor(Color.GREEN);
                 holder.body.setBackgroundResource(R.drawable.bubble2);
                 holder.authorName.setText("You");
             }
             else{
                 holder.params.gravity = Gravity.START;
                 holder.params.rightMargin = 150;
                 holder.params.leftMargin = 0;
                 holder.authorName.setTextColor(Color.BLUE);
                 holder.body.setBackgroundResource(R.drawable.bubble1);
                 String author = instantMessage.getUser();
                 holder.authorName.setText(author);

             }
             holder.authorName.setLayoutParams(holder.params);
             holder.body.setLayoutParams(holder.params);


         }
        catch (NullPointerException e){e.printStackTrace();}

//         if(isItMe){
//             holder.authorName.setText("You");
//         }
//         else{
//
////             String author = instantMessage.getUser();
////             holder.authorName.setText(author);
//         }

       // catch (NullPointerException e){e.printStackTrace();}

        ;


        String message = instantMessage.getInputMsg();
        holder.body.setText(message);

        return convertView;
    }

   // private void setChatRowAppearance(boolean isItMe , ViewHolder holder)


    public void cleanUp(){
        mDatabaseReference.removeEventListener(mListener);
    }
}
