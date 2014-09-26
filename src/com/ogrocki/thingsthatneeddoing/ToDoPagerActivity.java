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
import java.util.UUID;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;

import com.ogrocki.thingsthatneeddoing.R;

public class ToDoPagerActivity extends FragmentActivity {
	private ViewPager mViewPager;
	private ArrayList<ToDo> mToDos;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mViewPager = new ViewPager(this);
		mViewPager.setId(R.id.viewPager);
		setContentView(mViewPager);

		mToDos = ToDoLab.get(this).getToDos();
		FragmentManager fm = getSupportFragmentManager();
		mViewPager.setAdapter(new FragmentStatePagerAdapter(fm) {
			@Override
			public int getCount() {
				return mToDos.size();
			}
			@Override
			public Fragment getItem(int pos) {
				ToDo toDo = mToDos.get(pos);
				return ToDoFragment.newInstance(toDo.getId());
			}
		});

		mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
			public void onPageScrollStateChanged(int state) { }
			public void onPageScrolled(int pos, float posOffset, int posOffsetPixels) { }
			public void onPageSelected(int pos) {
				ToDo toDo = mToDos.get(pos);
				if (toDo.getTitle() != null) {
					setTitle(toDo.getTitle());
				}
			}
		});


		UUID toDoId = (UUID)getIntent()
				.getSerializableExtra(ToDoFragment.EXTRA_ID);
		for (int i = 0; i < mToDos.size(); i++) {
			if (mToDos.get(i).getId().equals(toDoId)) {
				mViewPager.setCurrentItem(i);
				break;
			}
		}
	}
}

