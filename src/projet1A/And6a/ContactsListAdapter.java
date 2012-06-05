package projet1A.And6a;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONArray;
import org.json.JSONException;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ContactsListAdapter extends BaseAdapter {
	
	private Context context;
	private ArrayList<Contact> contacts;
	
	ContactsListAdapter(Context context, JSONArray jsonArray) throws ClientProtocolException, JSONException, IOException {
		this.context = context;
		this.contacts = new ArrayList<Contact>();
		
		for (int i = 0; i<jsonArray.length(); i++) {
			this.contacts.add(Contact.createSimpleContact(jsonArray.getJSONObject(i)));
		}
	
	}

	public int getCount() {
		return this.contacts.size();
	}

	public Object getItem(int position) {
		return this.contacts.get(position);
	}

	public long getItemId(int position) {
		return this.contacts.get(position).getId();
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		Contact contact = this.contacts.get(position);

		LinearLayout layout = new LinearLayout(context);
		layout.setOrientation(LinearLayout.VERTICAL);

		TextView nameAndLastname = new TextView(context);
		nameAndLastname.setTextAppearance(context, android.R.style.TextAppearance_Medium);
		StringBuilder string = new StringBuilder();
		string.append(contact.getName());
		string.append(" ");
		string.append(contact.getLastName());
		nameAndLastname.setText(string.toString());		
		layout.addView(nameAndLastname);

		String tel = contact.getFirstTel();
		if (!tel.equals("null")) {
			TextView textView = new TextView(context);
			textView.setText(tel);
			layout.addView(textView);
		}
		
		String email = contact.getFirstEmail();
		if (!email.equals("null")) {
			TextView textView = new TextView(context);
			textView.setText(email);
			layout.addView(textView);
		}
	
		return layout;
	}
	
}