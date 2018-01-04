package com.example.android.waitlist;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.example.android.waitlist.data.TestUtil;
import com.example.android.waitlist.data.WaitlistContract;
import com.example.android.waitlist.data.WaitlistDbHelper;

import static com.example.android.waitlist.data.WaitlistContract.*;


public class MainActivity extends AppCompatActivity {

    private GuestListAdapter mAdapter;
    private SQLiteDatabase mDb;


    EditText mNewGuestNameEditText;
    EditText mNewPartySizeEditText;

    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView waitlistRecyclerView;

        // Set local attributes to corresponding views
        waitlistRecyclerView = (RecyclerView) this.findViewById(R.id.all_guests_list_view);

        mNewGuestNameEditText = (EditText) findViewById(R.id.person_name_edit_text);
        mNewPartySizeEditText = (EditText) findViewById(R.id.party_count_edit_text);

        // Set layout for the RecyclerView, because it's a list we are using the linear layout
        waitlistRecyclerView.setLayoutManager(new LinearLayoutManager(this));


        // Create a DB helper (this will create the DB if run for the first time)
        WaitlistDbHelper dbHelper = new WaitlistDbHelper(this);

        // Keep a reference to the mDb until paused or killed. Get a writable database
        // because you will be adding restaurant customers
        mDb = dbHelper.getWritableDatabase();

        // Get all guest info from the database and save in a cursor
        Cursor cursor = getAllGuests();

        // Create an adapter for that cursor to display the data
        mAdapter = new GuestListAdapter(this, cursor);

        // Link the adapter to the RecyclerView
        waitlistRecyclerView.setAdapter(mAdapter);

    }

    /**
     * This method is called when user clicks on the Add to waitlist button
     *
     * @param view The calling view (button)
     */
    public void addToWaitlist(View view) {

        String guestName = mNewGuestNameEditText.getText().toString();
        String sizeAsString = mNewPartySizeEditText.getText().toString();

        if(guestName.isEmpty() || sizeAsString.isEmpty())
            return;

        int partySize = 1;
        try {
            partySize = Integer.parseInt(sizeAsString);
        } catch (Exception e) {
            Log.d(LOG_TAG, e.getMessage());
        }

        addGuest(guestName, partySize);

        mAdapter.swapCursor(getAllGuests());

        mNewGuestNameEditText.getText().clear();
        mNewPartySizeEditText.getText().clear();

        mNewPartySizeEditText.clearFocus();
    }



    /**
     * Query the mDb and get all guests from the waitlist table
     *
     * @return Cursor containing the list of guests
     */
    private Cursor getAllGuests() {
        return mDb.query(
                WaitlistEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                WaitlistEntry.COLUMN_TIMESTAMP
        );
    }

    private void addGuest(String guestName, int partySize) {
        ContentValues contentValues = new ContentValues();

        contentValues.put(WaitlistEntry.COLUMN_GUEST_NAME, guestName);
        contentValues.put(WaitlistEntry.COLUMN_PARTY_SIZE, partySize);

        mDb.insert(WaitlistEntry.TABLE_NAME, null, contentValues);
    }



}