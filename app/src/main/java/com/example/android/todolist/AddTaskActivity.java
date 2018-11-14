/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.todolist;

import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.android.todolist.data.TaskContract;


public class AddTaskActivity extends AppCompatActivity {

    // Declare a member variable to keep track of a task's selected mPriority
    private int mPriority;

    private int taskId;

    private EditText editTextTaskDescription;

    private RadioGroup radioGroupTaskPriority;

    private Button actionButton;

    private boolean isUpdate = false;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        // Initialize to highest mPriority by default (mPriority = 1)
        ((RadioButton) findViewById(R.id.radButton1)).setChecked(true);
        mPriority = 1;

        editTextTaskDescription = (EditText) findViewById(R.id.editTextTaskDescription);
        radioGroupTaskPriority = (RadioGroup) findViewById(R.id.priority_rg);
        actionButton = (Button) findViewById(R.id.addButton);

        Intent mIntent = getIntent();

        if(mIntent.hasExtra(getString(R.string.item_text))){
            String itemText = mIntent.getStringExtra(getString(R.string.item_text));
            taskId = mIntent.getIntExtra(getString(R.string.item_id),-1);
            mPriority = mIntent.getIntExtra(getString(R.string.item_priority),-1);

            switch (mPriority){
                case 1:
                    radioGroupTaskPriority.check(R.id.radButton1);
                    break;
                case 2:
                    radioGroupTaskPriority.check(R.id.radButton2);
                    break;
                case 3:
                    radioGroupTaskPriority.check(R.id.radButton3);
                    break;

                default:
                    Toast.makeText(this,"mPriority = " + mPriority,Toast.LENGTH_LONG).show();
                    break;
            }

            actionButton.setText(getString(R.string.update_button));
            editTextTaskDescription.setText(itemText);
            isUpdate = true;
        }
        else {
            actionButton.setText(getString(R.string.add_button));
            isUpdate = false;
        }
    }


    /**
     * onClickAddTask is called when the "ADD" button is clicked.
     * It retrieves user input and inserts that new task data into the underlying database.
     */
    public void onClickAddTask(View view) {
        // Not yet implemented
        // Check if EditText is empty, if not retrieve input and store it in a ContentValues object
        // If the EditText input is empty -> don't create an entry
        String input = ((EditText) findViewById(R.id.editTextTaskDescription)).getText().toString();
        if (input.length() == 0) {
            return;
        }

        // Insert new task data via a ContentResolver
        // Create new empty ContentValues object
        ContentValues contentValues = new ContentValues();
        // Put the task description and selected mPriority into the ContentValues
        contentValues.put(TaskContract.TaskEntry.COLUMN_DESCRIPTION, input);
        contentValues.put(TaskContract.TaskEntry.COLUMN_PRIORITY, mPriority);

        Uri uri = null;
        int rowsUpdated = 0;
        if(isUpdate == false) {
            // Insert the content values via a ContentResolver
            uri = getContentResolver().insert(TaskContract.TaskEntry.CONTENT_URI, contentValues);
        }
        else {
            Uri mUri = TaskContract.TaskEntry.CONTENT_URI;
            mUri = mUri.buildUpon().appendPath(Integer.toString(taskId)).build();
            rowsUpdated = getContentResolver().update(mUri,contentValues,null,null);
        }
        // Display the URI that's returned with a Toast
        // [Hint] Don't forget to call finish() to return to MainActivity after this insert is complete
        if(uri != null) {
            //Toast.makeText(getBaseContext(), uri.toString(), Toast.LENGTH_LONG).show();
        }
        else {
            // Toast.makeText(getBaseContext(),"Rows Updated = " + rowsUpdated, Toast.LENGTH_LONG).show();
        }

        // Finish activity (this returns back to MainActivity)
        finish();

    }


    /**
     * onPrioritySelected is called whenever a priority button is clicked.
     * It changes the value of mPriority based on the selected button.
     */
    public void onPrioritySelected(View view) {
        if (((RadioButton) findViewById(R.id.radButton1)).isChecked()) {
            mPriority = 1;
        } else if (((RadioButton) findViewById(R.id.radButton2)).isChecked()) {
            mPriority = 2;
        } else if (((RadioButton) findViewById(R.id.radButton3)).isChecked()) {
            mPriority = 3;
        }
    }
}
