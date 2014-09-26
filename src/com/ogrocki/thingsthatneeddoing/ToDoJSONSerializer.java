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

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONTokener;

import android.content.Context;

public class ToDoJSONSerializer {
	private Context mContext;
	public ToDoJSONSerializer(Context c) {
		mContext = c;

	}
	public ArrayList<ToDo> loadToDos(String filename) throws IOException, JSONException {
		ArrayList<ToDo> toDos = new ArrayList<ToDo>();
		BufferedReader reader = null;
		try {
			InputStream in = mContext.openFileInput(filename);
			reader = new BufferedReader(new InputStreamReader(in));
			StringBuilder jsonString = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				jsonString.append(line);
			}
			JSONArray array = (JSONArray) new JSONTokener(jsonString.toString())
			.nextValue();
			for (int i = 0; i < array.length(); i++) {
				toDos.add(new ToDo(array.getJSONObject(i)));
			}
		} catch (FileNotFoundException e) {
		} finally {
			if (reader != null)
				reader.close();
		}
		return toDos;
	}

	public void saveToDos(ArrayList<ToDo> toDos, String filename)
			throws JSONException, IOException {
		// Build an array in JSON
		JSONArray array = new JSONArray();
		for (ToDo c : toDos)
			array.put(c.toJSON());
		// Write the file to disk
		Writer w = null;
		try {
			OutputStream out = mContext
					.openFileOutput(filename, Context.MODE_PRIVATE);
			w = new OutputStreamWriter(out);
			w.write(array.toString());
		} finally {
			if (w != null)
				w.close();
		}
	}

}
