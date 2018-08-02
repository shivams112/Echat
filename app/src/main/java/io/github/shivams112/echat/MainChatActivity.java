package io.github.shivams112.echat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainChatActivity extends AppCompatActivity {

    // TODO: Add member variables here:
    private String mDisplayName;
    private ListView mChatListView;
    private EditText mInputText;
    private ImageButton mSendButton;
    private DatabaseReference mDatabaseReference;
    private ChatListAdapter mListAdapter;


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu ,menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        if(item.getItemId() == R.id.signOut){
            FirebaseAuth.getInstance().signOut();
            mDisplayName = null;
            Toast.makeText(this, "Signing out..", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(MainChatActivity.this ,LoginActivity.class);
            finish();
            startActivity(intent);
        return true;
        }
        else return  false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_chat);

        // TODO: Set up the display name and get the Firebase reference
        getDisplayName();
        mDatabaseReference = FirebaseDatabase.getInstance().getReference();



        // Link the Views in the layout to the Java code
        mInputText =  findViewById(R.id.messageInput);
        mSendButton =  findViewById(R.id.sendButton);
        mChatListView =  findViewById(R.id.chat_list_view);

        // TODO: Send the message when the "enter" button is pressed
        mInputText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                sendMessage();
                return true;
            }
        });


        // TODO: Add an OnClickListener to the sendButton to send a message
        mSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
            }
        });

    }

    // TODO: Retrieve the display name from the Shared Preferences
    private void getDisplayName(){
        SharedPreferences preferences = getSharedPreferences(RegisterActivity.CHAT_PREFS ,MODE_PRIVATE);
      mDisplayName=  preferences.getString(RegisterActivity.DISPLAY_NAME_KEY ,null);
        if(mDisplayName=="") mDisplayName = "Anonymous";

    }


    private void sendMessage() {
        Log.d("Echat" , "Something is sent");
        // TODO: Grab the text the user typed in and push the message to Firebase
        String input = mInputText.getText().toString();
        if(!input.equals("")){
             InstantMessage chat = new InstantMessage(input ,mDisplayName);
             mDatabaseReference.child("message").push().setValue(chat);
             mInputText.setText("");

        }

    }

    // TODO: Override the onStart() lifecycle method. Setup the adapter here.
    @Override
    public void onStart(){
        super.onStart();
        mListAdapter = new ChatListAdapter(this ,mDatabaseReference ,mDisplayName);
        mChatListView.setAdapter(mListAdapter);
    }


    @Override
    public void onStop() {
        super.onStop();

        // TODO: Remove the Firebase event listener on the adapter.
        mListAdapter.cleanUp();

    }

}
