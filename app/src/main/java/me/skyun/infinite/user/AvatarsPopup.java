package me.skyun.infinite.user;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.PopupWindow;

import me.skyun.infinite.R;
import me.skyun.utils.Utils;
import me.skyun.widget.ImageViewEx;

/**
 * Created by linyun on 16/7/25.
 */
public class AvatarsPopup extends PopupWindow {

    public interface OnAvatarClickListener {
        void onAvatarClick(String avatarUrl);
    }

    private OnAvatarClickListener mOnAvatarClickListener;

    public void setOnAvatarClickListener(
            OnAvatarClickListener onAvatarClickListener) {
        mOnAvatarClickListener = onAvatarClickListener;
    }

    public AvatarsPopup(Context context) {
        super(context);

        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        setOutsideTouchable(true);

        View view = View.inflate(context, R.layout.popup_avatars, null);
        GridView avatarGrid = (GridView) view.findViewById(R.id.avatars_grid);
        avatarGrid.setNumColumns(4);
        avatarGrid.setHorizontalSpacing(20);
        avatarGrid.setAdapter(new AvatarAdapter(context));

        setContentView(view);
    }

    private class AvatarAdapter extends ArrayAdapter<String> {

        public AvatarAdapter(Context context) {
            super(context, 0);
        }

        @Override
        public int getCount() {
            return 4;
        }

        @Override
        public String getItem(int position) {
            return "/default_avatars/default_avatar_" + (position + 1) + ".jpg";
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ImageViewEx avatarView;
            if (convertView == null) {
                convertView = View.inflate(parent.getContext(), R.layout.item_avatar, null);
                avatarView = (ImageViewEx) convertView.findViewById(R.id.avatar);
                avatarView.setOval(true);
                convertView.setTag(avatarView);
                convertView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mOnAvatarClickListener != null) {
                            mOnAvatarClickListener.onAvatarClick(getItem(position));
                        }
                        dismiss();
                    }
                });
            } else {
                avatarView = (ImageViewEx) convertView.getTag();
            }
            String remoteUrl = getItem(position);
            int avatarDimen = Utils.dp2px(parent.getContext(), 45);
            avatarView.setRemoteUrl(remoteUrl, avatarDimen, avatarDimen);
            return convertView;
        }
    }
}
