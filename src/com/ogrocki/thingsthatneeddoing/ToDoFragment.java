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

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;

import com.ogrocki.thingsthatneeddoing.R;

public class ToDoFragment extends Fragment {
	private ToDo mToDo;
	private Button mDoneButton;
	private EditText mTitleField;
	private CheckBox mSolvedCheckBox;
	public static final String EXTRA_ID =
			"com.ogrocki.thingsthatneeddoing.todo_id";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		UUID toDoId = (UUID)getArguments().getSerializable(EXTRA_ID);

		mToDo = ToDoLab.get(getActivity()).getToDo(toDoId);

	}
	@Override
	public void onPause() {
		super.onPause();
		ToDoLab.get(getActivity()).saveToDos();
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup parent,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.todo_fragment, parent, false);
		
		
		mTitleField = (EditText)v.findViewById(R.id.todo_title);
		mTitleField.setText(mToDo.getTitle());
		mTitleField.addTextChangedListener(new TextWatcher() {
			public void onTextChanged(
					CharSequence c, int start, int before, int count) {
				mToDo.setTitle(c.toString());
			}
			public void beforeTextChanged(
					CharSequence c, int start, int count, int after) {
				// This space intentionally left blank
			}
			public void afterTextChanged(Editable c) {
				// This one too
			}
		});

		mSolvedCheckBox = (CheckBox)v.findViewById(R.id.todo_solved);
		mSolvedCheckBox.setChecked(mToDo.isSolved());
		mSolvedCheckBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// Set todo to done
				mToDo.setDone(isChecked);
			}
		});
		
		mDoneButton= (Button)v.findViewById(R.id.todo_done);
		mDoneButton.setClickable(mToDo.isSolved());
		mDoneButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	getActivity().finish();
            }
        });

		return v;
	}
	public static ToDoFragment newInstance(UUID toDoId) {
		Bundle args = new Bundle();
		args.putSerializable(EXTRA_ID, toDoId);
		ToDoFragment fragment = new ToDoFragment();
		fragment.setArguments(args);
		return fragment;
	}

}
