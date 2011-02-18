/* 
 * Copyright 2011 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.galactogolf;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.galactogolf.R;
import com.galactogolf.genericobjectmodel.levelloader.LevelDefinition;
import com.galactogolf.specificobjectmodel.GalactoGolfLevelResult;

/*
 * Adapter for showing the list view of the scores of a particular level
 */
public class LevelScoreAdapter extends ArrayAdapter<LevelDefinition> {
	public LevelScoreAdapter(Context context, int textViewResourceId,
			ArrayList<LevelDefinition> definitions) {
		super(context, textViewResourceId, definitions);
		this._data = definitions;
	}

	private ArrayList<LevelDefinition> _data = new ArrayList<LevelDefinition>();

	public void addItem(final LevelDefinition item) {
		_data.add(item);
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return _data.size();
	}

	@Override
	public LevelDefinition getItem(int position) {
		return (LevelDefinition) _data.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v = convertView;
		if (v == null) {
			LayoutInflater vi = (LayoutInflater) getContext().getSystemService(
					Context.LAYOUT_INFLATER_SERVICE);
			v = vi.inflate(R.layout.level_score_row, null);
		}
		LevelDefinition o = _data.get(position);
		if (o != null) {
			TextView title = (TextView) v
					.findViewById(R.id.level_score_number_label);
			if (title != null) {
				title.setText("" + (position + 1));
			}
			TextView score = (TextView) v
					.findViewById(R.id.level_score_score_label);
			if (score != null) {
				if (o.getResult() != null) {
					score.setText("Score: "
							+ ((GalactoGolfLevelResult) o.getResult()).score
							+ "("
							+ o.getPar()
							+ ") "
							+ (((GalactoGolfLevelResult) o.getResult()).score - o
									.getPar()));
				} else {
					score.setText("-");

				}
			}
		}
		return v;
	}

}
