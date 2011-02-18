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

import com.galactogolf.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/*
 * Used to pop up small message notifications over the game screen
 */
public class MessagePopupActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.message_popup_layout);

		Intent callingIntent = this.getIntent();
		String title = callingIntent
				.getStringExtra(UIConstants.PARAM_MESSAGE_TITLE);
		String body = callingIntent
				.getStringExtra(UIConstants.PARAM_MESSAGE_BODY);

		TextView messageTitle = (TextView) findViewById(R.id.message_popup_title_label);
		TextView messageBody = (TextView) findViewById(R.id.message_popup_body_label);

		messageTitle.setText(title);
		messageBody.setText(body);

		Button okButton = (Button) findViewById(R.id.message_popup_ok_button);

		okButton.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				finish();
			}
		});
	}
}
