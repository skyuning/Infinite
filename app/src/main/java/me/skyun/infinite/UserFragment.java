//package me.skyun.infinite;
//
//import android.os.Bundle;
//import android.support.v4.app.Fragment;
//import android.view.LayoutInflater;
//import android.view.MotionEvent;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.BaseAdapter;
//import android.widget.EditText;
//import android.widget.ListView;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.raizlabs.android.dbflow.list.FlowCursorList;
//import com.raizlabs.android.dbflow.sql.language.SQLite;
//
//import me.skyun.infinite.user.Role;
//import me.skyun.widget.ImageViewEx;
//
///**
// * Created by linyun on 16/6/1.
// */
//
//public class UserFragment extends Fragment {
//
//    private ImageViewEx mBannerView;
//    private EditText mRoleNameView;
//    private EditText mRoleAgeView;
//    private View mCreateView;
//    private ListView mRoleListView;
//    private RoleAdapter mAdapter;
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.fragment_user, null);
//        return view;
//    }
//
//    @Override
//    public void onViewCreated(View view, Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
//        mBannerView = (ImageViewEx) view.findViewById(R.id.user_iv_banner);
//        mRoleNameView = (EditText) view.findViewById(R.id.user_et_role_name);
//        mRoleAgeView = (EditText) view.findViewById(R.id.user_et_role_age);
//        mCreateView = view.findViewById(R.id.user_tv_create);
//        mRoleListView = (ListView) view.findViewById(R.id.user_list_roles);
//
//        mCreateView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Role role = new Role();
//                role.name = mRoleNameView.getText().toString();
//                if (role.name.isEmpty()) {
//                    Toast.makeText(v.getContext(), "请输入名字", Toast.LENGTH_SHORT).show();
//                    return;
//                }
//                try {
//                    role.age = Integer.parseInt(mRoleAgeView.getText().toString());
//                } catch (NumberFormatException e) {
//                    e.printStackTrace();
//                    Toast.makeText(v.getContext(), "请输入正确的年龄", Toast.LENGTH_SHORT).show();
//                    return;
//                }
//                role.save();
//                mAdapter.notifyDataSetChanged();
//            }
//        });
//
//        mAdapter = new RoleAdapter();
//        mRoleListView.setAdapter(mAdapter);
//
//        view.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                return mBannerView.onTouchEvent(event);
//            }
//        });
//    }
//
//    private class RoleAdapter extends BaseAdapter {
//
//        private boolean mEditable = false;
//
//        private FlowCursorList<Role> mCursorList;
//
//        public RoleAdapter() {
//            mCursorList = new FlowCursorList<>(100, Role.class);
//        }
//
//        public void setEditable(boolean editable) {
//            mEditable = editable;
//        }
//
//        @Override
//        public void notifyDataSetChanged() {
//            super.notifyDataSetChanged();
//            mCursorList = new FlowCursorList<>(100, Role.class);
//        }
//
//        @Override
//        public int getCount() {
//            return mCursorList.getCount();
//        }
//
//        @Override
//        public Object getItem(int position) {
//            return mCursorList.getItem(position);
//        }
//
//        @Override
//        public long getItemId(int position) {
//            return position;
//        }
//
//        @Override
//        public View getView(int position, View convertView, ViewGroup parent) {
//            ViewHolder holder;
//            if (convertView == null) {
//                holder = new ViewHolder();
//                convertView = View.inflate(parent.getContext(), R.layout.item_role, null);
//                holder.mNameView = (TextView) convertView.findViewById(R.id.role_item_tv_name);
//                holder.mAgeView = (TextView) convertView.findViewById(R.id.role_item_tv_age);
//                holder.mDelView = convertView.findViewById(R.id.role_item_iv_delete);
//                convertView.setTag(holder);
//            } else {
//                holder = (ViewHolder) convertView.getTag();
//            }
//            final Role role = (Role) getItem(position);
//            holder.mNameView.setText(role.name);
//            holder.mAgeView.setText("" + role.age);
//            holder.mDelView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    SQLite.delete(Role.class).where(Role_Table.id.is(role.id)).query();
//                    mAdapter.notifyDataSetChanged();
//                }
//            });
//            return convertView;
//        }
//    }
//
//    private class ViewHolder {
//        public TextView mNameView;
//        public TextView mAgeView;
//        public View mDelView;
//    }
//}
