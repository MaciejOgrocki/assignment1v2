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

import java.util.ArrayList;
import java.util.Collections;
import java.util.UUID;

import android.content.Context;

public class ToDoLab {
	private static final String TAG = "ToDoLab";
	private ToDoJSONSerializer mSerializer;
	private static ToDoLab sToDoLab;
	private Context mAppContext;
	private static final String FILENAME = "tododata.json";
	private static final String ARCHIVE_FILENAME = "arch_tododata.json";
	private ArrayList<ToDo> mToDos;
	private ArrayList<ToDo> mArchivedToDos;


	private ToDoLab(Context appContext) {
		mAppContext = appContext;
		mToDos = new ArrayList<ToDo>();
		mSerializer = new ToDoJSONSerializer(mAppContext);
		mToDos = new ArrayList<ToDo>();
		try {
			mToDos = mSerializer.loadToDos(FILENAME);
		} catch (Exception e) {
			mToDos = new ArrayList<ToDo>();
		}
		
		try {
			mArchivedToDos = mSerializer.loadToDos(ARCHIVE_FILENAME);
		} catch (Exception e) {
			mArchivedToDos = new ArrayList<ToDo>();
		}
		

	}

	public static ToDoLab get(Context c) {
		if (sToDoLab == null) {
			sToDoLab = new ToDoLab(c.getApplicationContext());
		}
		return sToDoLab;

	}
	
	public void deleteToDo(ToDo c) {
		mToDos.remove(c);
	}
	public void addToDo(ToDo c) {
		mToDos.add(c);
	}
	public boolean saveToDos() {
		try {
			mSerializer.saveToDos(mToDos, FILENAME);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	public ToDo getToDo(UUID id) {
		for (ToDo c : mToDos) {
			if (c.getId().equals(id))
				return c;
		}
		return null;
	}
	public ArrayList<ToDo> getToDos() {
		return mToDos;
	}
	
	public void deleteArchivedToDo(ToDo c) {
		mArchivedToDos.remove(c);
	}
	public void addArchivedToDo(ToDo c) {	
		mArchivedToDos.add(c);
	}
	public boolean saveArchivedToDos() {
		try {
			mSerializer.saveToDos(mArchivedToDos, ARCHIVE_FILENAME);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	public ToDo getArchivedToDo(UUID id) {
		for (ToDo c : mArchivedToDos) {
			if (c.getId().equals(id))
				return c;
		}
		return null;
	}
	public ArrayList<ToDo> getArchivedToDos() {
		return mArchivedToDos;
	}

	public void moveToArchive(ToDo c){
		mToDos.remove(c);
		mArchivedToDos.add(0,c);
	}
	public void moveToActive(ToDo c){
		mArchivedToDos.remove(c);
		mToDos.add(0,c);
	}
}

