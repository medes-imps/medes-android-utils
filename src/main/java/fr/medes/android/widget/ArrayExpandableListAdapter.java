package fr.medes.android.widget;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;

import java.util.List;

/**
 * An easy adapter to map static data to group and child views defined in an XML file. You can separately specify the
 * data backing the group as a List of Groups. Each entry in the ArrayList corresponds to one group in the expandable
 * list. The Group contain the data for each row. You also specify an XML file that defines the views used to display a
 * group. This process is similar for a child, except it is one-level deeper so the data backing is specified as a
 * List<List<Child>>, where the first List corresponds to the group of the child, the second List corresponds to the
 * position of the child within the group, and finally the Child holds the data for that particular child.
 */
public class ArrayExpandableListAdapter<Group, Child> extends BaseExpandableListAdapter {

	private List<Group> mGroupData;
	private int mGroupLayout;
	private List<List<Child>> mChildData;
	private int mChildLayout;

	private ViewBinder<Group, Child> mViewBinder;

	private LayoutInflater mInflater;

	/**
	 * Constructor
	 * 
	 * @param context The context where the {@link ExpandableListView} associated with this
	 *            {@link ArrayExpandableListAdapter} is running
	 * @param groupData A list of groups. Each entry in the List corresponds to one group in the list
	 * @param childData A List of List of children. Each entry in the outer List corresponds to a group (index by group
	 *            position), each entry in the inner List corresponds to a child within the group (index by child
	 *            position)
	 */
	public ArrayExpandableListAdapter(Context context, List<Group> groupData, List<List<Child>> childData) {
		this(context, groupData, android.R.layout.simple_expandable_list_item_1, childData,
				android.R.layout.simple_expandable_list_item_2);
	}

	/**
	 * Constructor
	 * 
	 * @param context The context where the {@link ExpandableListView} associated with this
	 *            {@link ArrayExpandableListAdapter} is running
	 * @param groupData A list of groups. Each entry in the List corresponds to one group in the list
	 * @param groupLayout resource identifier of a view layout that defines the views for a group
	 * @param childData A List of List of children. Each entry in the outer List corresponds to a group (index by group
	 *            position), each entry in the inner List corresponds to a child within the group (index by child
	 *            position)
	 * @param childLayout resource identifier of a view layout that defines the views for a child
	 */
	public ArrayExpandableListAdapter(Context context, List<Group> groupData, int groupLayout,
			List<List<Child>> childData, int childLayout) {
		mInflater = LayoutInflater.from(context);
		mGroupData = groupData;
		mGroupLayout = groupLayout;
		mChildData = childData;
		mChildLayout = childLayout;
	}

	/**
	 * Returns the {@link ViewBinder} used to bind data to views.
	 * 
	 * @return a ViewBinder or null if the binder does not exist
	 * 
	 * @see #bindGroupView(View, Object)
	 * @see #bindChildView(View, Object)
	 * @see #setViewBinder(ViewBinder)
	 */
	public ViewBinder<Group, Child> getViewBinder() {
		return mViewBinder;
	}

	/**
	 * Sets the binder used to bind data to views.
	 * 
	 * @param viewBinder the binder used to bind data to views, can be null to remove the existing binder
	 * 
	 * @see #bindGroupView(View, Object)
	 * @see #bindChildView(View, Object)
	 * @see #getViewBinder()
	 */
	public void setViewBinder(ViewBinder<Group, Child> viewBinder) {
		mViewBinder = viewBinder;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
		View view;
		if (convertView != null) {
			view = convertView;
		} else {
			view = newGroupView(parent);
		}
		bindGroupView(view, getGroup(groupPosition));
		return view;
	}

	/**
	 * Makes a new group view to hold the group data.
	 * 
	 * @param parent The parent to which the new view is attached to
	 * @return the newly created view.
	 */
	public View newGroupView(ViewGroup parent) {
		return mInflater.inflate(mGroupLayout, parent, false);
	}

	/**
	 * Bind an existing view to the group data
	 * 
	 * @param view Existing view, returned earlier by newView
	 * @param group The group from which to get the data
	 */
	public void bindGroupView(View view, Group group) {
		final ViewBinder<Group, Child> binder = mViewBinder;

		boolean bound = false;
		if (binder != null) {
			bound = binder.setGroupViewValue(view, group);
		}

		if (!bound) {
			TextView text = (TextView) view.findViewById(android.R.id.text1);
			text.setText((Integer) group);
		}
	}

	@Override
	public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView,
			ViewGroup parent) {
		View view;

		if (convertView != null) {
			view = convertView;
		} else {
			view = newChildView(parent);
		}
		bindChildView(view, getChild(groupPosition, childPosition));
		return view;
	}

	/**
	 * Makes a new child view to hold the child data.
	 * 
	 * @param parent The parent to which the new view is attached to
	 * @return the newly created view.
	 */
	public View newChildView(ViewGroup parent) {
		return mInflater.inflate(mChildLayout, parent, false);
	}

	/**
	 * Bind an existing view to the child data
	 * 
	 * @param view Existing view, returned earlier by newView
	 * @param child The child from which to get the data
	 */
	public void bindChildView(View view, Child child) {
		final ViewBinder<Group, Child> binder = mViewBinder;

		boolean bound = false;
		if (binder != null) {
			bound = binder.setChildViewValue(view, child);
		}

		if (!bound) {
			TextView text = (TextView) view.findViewById(android.R.id.text1);
			text.setText(((int[]) child)[0]);
			text = (TextView) view.findViewById(android.R.id.text2);
			text.setText(((int[]) child)[1]);
		}
	}

	@Override
	public Child getChild(int groupPosition, int childPosition) {
		return mChildData.get(groupPosition).get(childPosition);
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		return mChildData.get(groupPosition).size();
	}

	@Override
	public Group getGroup(int groupPosition) {
		return mGroupData.get(groupPosition);
	}

	@Override
	public int getGroupCount() {
		return mGroupData.size();
	}

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	@Override
	public boolean hasStableIds() {
		return true;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}

	/**
	 * This class can be used by external clients of ArrayExpandableListAdapter to bind values from the lists to views.
	 * 
	 * You should use this class to bind values from the lists to views that are not directly supported by
	 * ArrayExpandableListAdapter or to change the way binding occurs for views supported by ArrayExpandableListAdapter.
	 */
	public interface ViewBinder<Group, Child> {
		/**
		 * Binds the list group item to the specified view.
		 * 
		 * When binding is handled by this ViewBinder, this method must return true. If this method returns false,
		 * ArrayExpandableListAdapter will attempts to handle the binding on its own.
		 * 
		 * @param view the view to bind the data to
		 * @param group the group item to get the data from
		 * 
		 * @return true if the data was bound to the view, false otherwise
		 */
		boolean setGroupViewValue(View view, Group group);

		/**
		 * Binds the list child item to the specified view.
		 * 
		 * When binding is handled by this ViewBinder, this method must return true. If this method returns false,
		 * ArrayExpandableListAdapter will attempts to handle the binding on its own.
		 * 
		 * @param view the view to bind the data to
		 * @param child the child item to get the data from
		 * 
		 * @return true if the data was bound to the view, false otherwise
		 */
		boolean setChildViewValue(View view, Child child);
	}

}
