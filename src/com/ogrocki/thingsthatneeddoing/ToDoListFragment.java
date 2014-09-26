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

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView.MultiChoiceModeListener;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ogrocki.thingsthatneeddoing.R;

public class ToDoListFragment extends ListFragment {
	private static final String TAG = "ToDoListFragment";
	private ArrayList<ToDo> mToDos;
	private ArrayList<ToDo> mArchivedToDos;
	private boolean inArchive = false;

	@Override
	public void onResume() {
		super.onResume();
		((ToDoAdapter)getListAdapter()).notifyDataSetChanged();
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.fragment_todo_list, menu);
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_item_new_todo:
			ToDo toDo = new ToDo();
			ToDoLab lab = ToDoLab.get(getActivity());

			if (inArchive)
				lab.addArchivedToDo(toDo);
			else
				lab.addToDo(toDo);

			Intent i = new Intent(getActivity(), ToDoPagerActivity.class);
			i.putExtra(ToDoFragment.EXTRA_ID, toDo.getId());
			startActivityForResult(i, 0);
			return true;

		case R.id.menu_go_to_archive:
			inArchive = !inArchive;
			setAdapter();
			// test if needed
			((ToDoAdapter)getListAdapter()).notifyDataSetChanged();
			return true;
		case R.id.menu_item_stats:
			int aChecked=0,aUnchecked=0, checked=0, unchecked=0;
			for(int x=0; x<mToDos.size(); x++){
				ToDo c = mToDos.get(x);
				if(mToDos.get(x).isSolved()){
					checked++;
				}
				else{
					unchecked++;
				}
			}
			for(int x=0; x<mArchivedToDos.size(); x++){
				ToDo c = mArchivedToDos.get(x);
				if(mArchivedToDos.get(x).isSolved()){
					aChecked++;
				}
				else{
					aUnchecked++;
				}
			}
			String stats = "Statistics\nTODO items checked: "+checked+"\nTODO items unchecked: "+unchecked+"\n\nArchived TODO items: "+mArchivedToDos.size()+"\nChecked archived TODO items: "+aChecked+"\nUnchecked archived TODO items: "+aUnchecked;
			Toast.makeText(getActivity(), stats,Toast.LENGTH_LONG).show();
			return true;
		case R.id.menu_item_email_all:
			String emailAll = "";
			for (int z = mArchivedToDos.size() - 1; z >= 0; z--){;
			if (mArchivedToDos.get(z).isSolved()==false)
				emailAll=emailAll + mArchivedToDos.get(z).getTitle() + " is Not Done and Archived\n";
			if (mArchivedToDos.get(z).isSolved()==true)
				emailAll=emailAll + mArchivedToDos.get(z).getTitle() + " is Done and Archived\n";				
			}
			for (int z = mToDos.size() - 1; z >= 0; z--){;
			if (mToDos.get(z).isSolved()==false)
				emailAll=emailAll + mToDos.get(z).getTitle() + " is Not Done\n";
			if (mToDos.get(z).isSolved()==true)
				emailAll=emailAll + mToDos.get(z).getTitle() + " is Done\n";				
			}
			Intent intent = new Intent(Intent.ACTION_SEND);
			intent.setType("text/plain");
			intent.putExtra(Intent.EXTRA_TEXT, emailAll);
			startActivity(intent);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
		View v = super.onCreateView(inflater, parent, savedInstanceState);
		ListView listView = (ListView)v.findViewById(android.R.id.list);

		// Use contextual action bar on Honeycomb and higher
		listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
		listView.setMultiChoiceModeListener(new MultiChoiceModeListener() {

			@Override
			public boolean onCreateActionMode(android.view.ActionMode mode,
					Menu menu) {
				MenuInflater inflater = mode.getMenuInflater();
				inflater.inflate(R.menu.todo_list_item_context, menu);
				return true;
			}
			
			@Override
			public boolean onPrepareActionMode(
					android.view.ActionMode mode, Menu menu) {
				//Done
				return false;
			}

			@Override
			public boolean onActionItemClicked(android.view.ActionMode mode, MenuItem item) {

				ToDoAdapter adapter = (ToDoAdapter)getListAdapter();
				ToDoLab lab = ToDoLab.get(getActivity());

				switch (item.getItemId()) {

				case R.id.menu_item_delete_todo:
					for (int i = adapter.getCount() - 1; i >= 0; i--) {
						if (getListView().isItemChecked(i)) {
							if (inArchive)
								lab.deleteArchivedToDo(adapter.getItem(i));
							else
								lab.deleteToDo(adapter.getItem(i));
						}
					}
					mode.finish();
					adapter.notifyDataSetChanged();
					return true;

				case R.id.menu_item_email_todo:
					String email = "-To Do App Email-\n";

					for (int z = adapter.getCount() - 1; z >= 0; z--) {
						if (!getListView().isItemChecked(z))
							continue;
						if (inArchive) {
							ToDo c = adapter.getItem(z);
							if (c.isSolved())
								email += c.getTitle() + " is in the Archive and Done\n";
							else
								email += c.getTitle() + " is in the Archive and Not Done\n";
								
						} else {
						ToDo c = adapter.getItem(z);
						if (c.isSolved())
							email += c.getTitle() + " is Done\n";
						else
							email += c.getTitle() + " is Not Done\n";
							
						}
					}
					mode.finish();
					Intent i = new Intent(Intent.ACTION_SEND);
					i.setType("text/plain");
					i.putExtra(Intent.EXTRA_TEXT, email);
					startActivity(i);
					return true;
				case R.id.menu_item_archive_todo:

					for (int Z = adapter.getCount() - 1; Z >= 0; Z--) {
						if (getListView().isItemChecked(Z)) {
							if (inArchive)
								lab.moveToActive(adapter.getItem(Z));
							else
								lab.moveToArchive(adapter.getItem(Z));
						}
					}
					mode.finish();

					adapter.notifyDataSetChanged();
					return true;

				default:
					return false;
				}
			}
			@Override
			public void onDestroyActionMode(android.view.ActionMode mode) {
				// Done
			}
			@Override
			public void onItemCheckedStateChanged(
					android.view.ActionMode mode, int position, long id,
					boolean checked) {
				// done
			}
		});

		return v;
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		getActivity().getMenuInflater().inflate(R.menu.todo_list_item_context, menu);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);

		ToDoLab toDolab = ToDoLab.get(getActivity());
		mToDos = toDolab.getToDos();
		mArchivedToDos = toDolab.getArchivedToDos();

		setAdapter();
	}

	private void setAdapter() {
		ToDoAdapter adapter;

		if(inArchive){
			getActivity().setTitle(R.string.archive_title);
			adapter = new ToDoAdapter(mArchivedToDos);
		}
		else {
			getActivity().setTitle(R.string.to_do_list);
			adapter = new ToDoAdapter(mToDos);
		}

		setListAdapter(adapter);
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		if (inArchive)
			return;
		ToDo c = ((ToDoAdapter)getListAdapter()).getItem(position);


		Intent i = new Intent(getActivity(), ToDoPagerActivity.class);
		i.putExtra(ToDoFragment.EXTRA_ID, c.getId());
		startActivity(i);
	}
	private class ToDoAdapter extends ArrayAdapter<ToDo> {
		public ToDoAdapter(ArrayList<ToDo> toDos) {
			super(getActivity(), 0, toDos);
		}
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// If we weren't given a view, inflate one
			if (convertView == null) {
				convertView = getActivity().getLayoutInflater()
						.inflate(R.layout.todo_item_list, null);
			}
			// Configure the view for this toDo
			ToDo c = getItem(position);
			TextView titleTextView =
					(TextView)convertView.findViewById(R.id.todo_list_item_titleTextView);
			titleTextView.setText(c.getTitle());

			CheckBox solvedCheckBox =
					(CheckBox)convertView.findViewById(R.id.todo_list_item_solvedCheckBox);
			solvedCheckBox.setChecked(c.isSolved());
			return convertView;
		}
	}
	@Override
	public void onPause() {
		super.onPause();
		ToDoLab manager = ToDoLab.get(getActivity());
		manager.saveToDos();
		manager.saveArchivedToDos();
	}

}
