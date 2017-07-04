package com.bba.ministries.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bba.ministries.Common.GlobalClass;
import com.bba.ministries.R;
import com.bba.ministries.Utils.AnimatedExpandableListView;

import java.util.ArrayList;

/**
 * Created by v-62 on 11/7/2016.
 */
public class ExpandableAdapter extends AnimatedExpandableListView.AnimatedExpandableListAdapter {
    private LayoutInflater inflater;

    private ArrayList<GroupItem> items;

    public ExpandableAdapter(Context context) {
        inflater = LayoutInflater.from(context);

    }
    public void setData(ArrayList<GroupItem> items) {
        this.items = items;
    }
    @Override
    public ChildItem getChild(int groupPosition, int childPosition) {
        return items.get(groupPosition).items.get(childPosition);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }




    @Override
    public View getRealChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ChildHolder holder;
        ChildItem item = getChild(groupPosition, childPosition);
        if (convertView == null) {
            holder = new ChildHolder();
            convertView = inflater.inflate(R.layout.list_item, parent, false);
            holder.title = (TextView) convertView.findViewById(R.id.expandedListItem);
            holder.lines = (ImageView) convertView.findViewById(R.id.lines);

            convertView.setTag(holder);
        } else {
            holder = (ChildHolder) convertView.getTag();
        }

        holder.title.setText(item.subcategoryname);




       // convertView.setBackgroundColor(Color.parseColor("#0d434e"));
        holder.lines.setImageResource(R.color.childlast);
        if(items.get(groupPosition).items.size()>0) {
            if (items.get(groupPosition).items.size() == childPosition+1) {
                holder.lines.setImageResource(android.R.color.darker_gray);

            }
        }

        if(GlobalClass.childSelection==item.subcategoryname)
        {
            convertView.setBackgroundResource(R.drawable.toolbar_color);

        }else
        {
            convertView.setBackgroundColor(Color.parseColor("#0d434e"));


        }


        // holder.hint.setText(item.hint);

       /* if(GlobalClass.groupSelection==groupPosition)
        {
            convertView.setBackgroundColor(Color.parseColor("#085464"));
        }

        if(GlobalClass.childSelection==childPosition)
        {
            convertView.setBackgroundResource(R.drawable.toolbar_color);
        }else
        {

            convertView.setBackgroundColor(Color.parseColor("#085464"));

        }*/
        return convertView;
    }

    @Override
    public int getRealChildrenCount(int groupPosition) {
        return items.get(groupPosition).items.size();
    }

    @Override
    public GroupItem getGroup(int groupPosition) {
        return items.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return items.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        GroupHolder holder;
        GroupItem item = getGroup(groupPosition);
        if (convertView == null) {
            holder = new GroupHolder();
            convertView = inflater.inflate(R.layout.list_group, parent, false);
            holder.title = (TextView) convertView.findViewById(R.id.listTitle);
            holder.lines = (View) convertView.findViewById(R.id.lines);
            holder.image=(ImageView) convertView.findViewById(R.id.image);
            holder.logo=(ImageView) convertView.findViewById(R.id.logo);
            convertView.setTag(holder);
        } else {
            holder = (GroupHolder) convertView.getTag();
        }




        if(GlobalClass.groupSelection==groupPosition)
        {
            convertView.setBackgroundResource(R.drawable.toolbar_color);
           // holder.lines.setBackgroundColor(android.R.color.black);

        }else
        {

            convertView.setBackgroundColor(Color.parseColor("#085464"));


        }



        holder.title.setText(item.categoryname);
        holder.logo.setImageResource(item.icons);

            if(items.get(groupPosition).items.size()==0)
            {
                //Log.i("GGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGG","GGGGGGGGGGGggGGGGGGGGGGGggGGGGGGGGGGGggGGGGGGGGGGGgg"+GlobalClass.groupSelection);
                holder.image.setImageResource(android.R.color.transparent);
            }else
            {
               // Log.i("GGGGGGGGGGGGGGGG","GGGGGGGGGGGgg"+GlobalClass.groupSelection);
                holder.image.setImageResource(R.drawable.leftarrow_white);
            }



        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public boolean isChildSelectable(int arg0, int arg1) {
        return true;
    }

   public static class ChildHolder {
        TextView title;
        TextView hint;
       ImageView lines;
    }

    public static class GroupHolder {
        TextView title;
        ImageView image,logo;
        public static View lines;
    }
    public static class GroupItem {
        public String categoryname;
        public String category_id;
        public int icons;

        public ArrayList<ChildItem> items = new ArrayList<ChildItem>();

        public GroupItem(String categoryname, ArrayList<ChildItem> items, int icons)
        {
            this.categoryname=categoryname;
            this.items=items;
            this.icons=icons;
        }
    }

    public static class ChildItem {
        public String subcategoryname;
        public String subcategory_id;
        public ChildItem(String subcategoryname)
        {
            this.subcategoryname=subcategoryname;

        }


    }
}

