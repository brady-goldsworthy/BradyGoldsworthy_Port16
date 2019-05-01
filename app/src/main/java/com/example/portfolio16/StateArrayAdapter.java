package com.example.portfolio16;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class StateArrayAdapter extends ArrayAdapter<State>
{
    //context is the activity where the ListView is displayed
    //states is the list of data to be displayed
    public StateArrayAdapter(Context context, List<State> states)
    {
        //call the super class constructor. The -1 indicates that
        //a custom layout is being used
        super(context, -1, states);
    }//end constructor


    //The getView method is called to get the View that displays
    //a ListView item's data. Must be overridden for the custom layout
    //
    //position is the ListView item's position
    //convertView is the View representing the ListView item
    //parent is the parent of the ListView item
    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        //get the State for the specified ListView position
        State state = getItem(position);

        //Reference to the list item's views
        ViewHolder viewHolder;

        //Is there a reusable ViewHolder from a ListView item that scrolled
        //offscreen?

        //If there is not a reusable ViewHolder, create one
        if( convertView == null )
        {
            //Create a new ViewHolder object
            viewHolder = new ViewHolder();

            //Get a layout inflater so the custom layout can be attached
            LayoutInflater inflater = LayoutInflater.from(getContext());

            //Attach the custom layout. false indicates
            convertView = inflater.inflate(R.layout.list_item, parent, false);


            //Set up the connections in the new ViewHolder
            viewHolder.stateTV =
                    (TextView)convertView.findViewById(R.id.stateTextView);
            viewHolder.stateNumTV =
                    (TextView)convertView.findViewById(R.id.stateNumberTextView);

            //store the ViewHolder with the ListView item
            convertView.setTag(viewHolder);
        }
        //There is a reusable ViewHolder
        else
        {
            //get the ViewHolder that was stored with the ListView item
            viewHolder = (ViewHolder)convertView.getTag();
        }

        //Populate the TextView's for the ListView item with data
        viewHolder.stateTV.setText("State: " + state.stateAbbr);
        viewHolder.stateNumTV.setText("Number: " + state.stateNum);

        //Return the completed ListView item to be displayed
        return convertView;
    }


    //Class for re-using views as list items scroll off and on the screen
    private static class ViewHolder
    {
        TextView stateTV, stateNumTV;
    }//end inner ViewHolder class
}//end StateArrayAdapter

