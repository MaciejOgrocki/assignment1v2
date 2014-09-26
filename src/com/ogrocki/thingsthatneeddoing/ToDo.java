/*Licensed to the Apache Software Foundation (ASF) under one
or more contributor license agreements.  See the NOTICE file
distributed with this work for additional information
regarding copyright ownership.  The ASF licenses this file
to you under the Apache License, Version 2.0 (the
"License"); you may not use this file except in compliance
with the License.  You may obtain a copy of the License at

  http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing,
software distributed under the License is distributed on an
"AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
KIND, either express or implied.  See the License for the
specific language governing permissions and limitations
under the License.*/
package com.ogrocki.thingsthatneeddoing;

import java.util.UUID;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class ToDo {

	private static final String TAG = "QuizActivity";
	private String mTitle;
	private boolean mSolved;
	private UUID mId;
	private static final String JSON_TITLE = "title";
	private static final String JSON_SOLVED = "solved";
	private static final String JSON_ID = "id";
	private static final String JSON_ARCHIVED = "archived";
	
	public String getTitle() {
		return mTitle;
	}
	public void setTitle(String title) {
		mTitle = title;
	}
	public UUID getId() {
		return mId;
	}
	public boolean isSolved() {
		return mSolved;
	}
	public void setDone(boolean solved) {
		mSolved = solved;
	}
	public ToDo() {
		mId = UUID.randomUUID();
	}
	public JSONObject toJSON() throws JSONException {
		JSONObject json = new JSONObject();
		Log.d(TAG, "Pre Save Archived");
		json.put(JSON_SOLVED, mSolved);
		Log.d(TAG, "Post Save Archived");
		json.put(JSON_ID, mId.toString());
		json.put(JSON_TITLE, mTitle);
		
		
		

		return json;
	}

	public ToDo(JSONObject json) throws JSONException {
		Log.d(TAG, "Pre Load Archived");
		mId = UUID.fromString(json.getString(JSON_ID));
		if (json.has(JSON_TITLE)) {
			mTitle = json.getString(JSON_TITLE);
		}
		mSolved = json.getBoolean(JSON_SOLVED);
		
		Log.d(TAG, "Post Load Archived");
	}


	@Override
	public String toString() {
		return mTitle;
	}



}
