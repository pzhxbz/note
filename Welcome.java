package com.example.pzhxbz.note;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by pzhxbz on 2016/5/17.
 */
public class Welcome extends Fragment {
    private ImageButton add;
    //ListView list;
    public ArrayList<HashMap<String, Object>> listData;
    MyAdapter listItemAdapter;
    ListView list;
    LayoutInflater layout;
    ViewGroup change;
    public interface welcomeThing
    {
        ArrayList<HashMap<String, Object>> getData();
        void deleteData(int pos,int position);
        void addClick();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.activity_main, container, false);
        Toast.makeText(getContext(),"hehe",Toast.LENGTH_SHORT).show();
        add=(ImageButton)view.findViewById(R.id.add);
        add.setOnClickListener(addOnClick);
        list = (ListView)view.findViewById(R.id.list_item);
        listItemAdapter=new MyAdapter(getContext());
        list.setAdapter(listItemAdapter);
        list.setOnItemClickListener(listPress);
        list.setOnScrollListener(listScroll);
        layout=inflater;
        change=container;
        return view;
    }
    ListView.OnItemClickListener listPress = new ListView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> arg0, View v,
                                final int position,long id) {
            // TODO Auto-generated method stub
            HashMap<String, Object> map = listData
                    .get(position);
            int pos = Integer.valueOf((map.get("id")
                    .toString()));
            String title=map.get("title").toString();
            String content=map.get("content").toString();
            ((welcomeThing) getActivity()).deleteData(pos, position);
            EditText tit=(EditText)getActivity().findViewById(R.id.edtitle);
            EditText con=(EditText)getActivity().findViewById(R.id.note);
            tit.setText(title);
            con.setText(content);
        }
    };
    private ImageButton.OnClickListener addOnClick=new ImageButton.OnClickListener(){
        public void onClick(View v){
            //Toast.makeText(getContext(),"nimabi",Toast.LENGTH_SHORT).show();
            AnimationSet animationSet = new AnimationSet(true);
            RotateAnimation rotateAnimation = new RotateAnimation(0,360+45,
                    Animation.RELATIVE_TO_SELF,0.5f,
                    Animation.RELATIVE_TO_SELF,0.5f);
            rotateAnimation.setDuration(750);
            animationSet.addAnimation(rotateAnimation);
            add.startAnimation(animationSet);
            animationSet.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                }
                @Override
                public void onAnimationRepeat(Animation animation) {
                }
                @Override
                public void onAnimationEnd(Animation animation) {
                    ((welcomeThing) getActivity()).addClick();
                }
            });
        }
    };
    private ListView.OnScrollListener listScroll=new ListView.OnScrollListener(){
        private boolean scrollFlag = false;// 标记是否滑动
        private int lastVisibleItemPosition;// 标记上次滑动位置
        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
// TODO Auto-generated method stub
            if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                scrollFlag = true;
            } else {
                scrollFlag = false;
            }
        }
        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            AnimationSet animationSet = new AnimationSet(true);
            if (scrollFlag) {
                if (firstVisibleItem < lastVisibleItemPosition) {
                    AlphaAnimation alphaAnimation = new AlphaAnimation(0,1);
                    alphaAnimation.setDuration(500);
                    animationSet.addAnimation(alphaAnimation);
                    alphaAnimation.setFillAfter(true);
                    add.startAnimation(alphaAnimation);
                }
                if (firstVisibleItem > lastVisibleItemPosition) {
                    AlphaAnimation alphaAnimation = new AlphaAnimation(1, 0);
                    alphaAnimation.setDuration(500);
                    animationSet.addAnimation(alphaAnimation);
                    alphaAnimation.setFillAfter(true);
                    add.startAnimation(alphaAnimation);
                }
                if (firstVisibleItem == lastVisibleItemPosition) {
                    return;
                }
                lastVisibleItemPosition = firstVisibleItem;
            }
        }
    };
    class MyAdapter extends BaseAdapter {
        private LayoutInflater mInflater;
        public MyAdapter(Context context) {
            this.mInflater = LayoutInflater.from(context);
            //super();
            //mInflater = (LayoutInflater) context
            //        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        @Override
        public Object getItem(int position) {
            return null;
        }
        @Override
        public int getCount() {
            return listData.size();
        }
        @Override
        public long getItemId(int position) {
            return 0;
        }
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            View myView = mInflater.inflate(R.layout.item, null);
            holder = new ViewHolder();
            final MainActivity activity=(MainActivity)(getActivity());
            listData=((welcomeThing) getActivity()).getData();
            holder.title = (TextView) myView.findViewById(R.id.title);
            holder.title.setText(listData.get(position).get("title").toString());
            holder.text = (TextView) myView.findViewById(R.id.content);
            holder.text.setText(listData.get(position).get("content").toString());
            holder.bt = (ImageButton) myView.findViewById(R.id.delete);
            holder.date = (TextView) myView.findViewById(R.id.date);
            holder.date.setText(listData.get(position).get("date").toString());
            final int arg = position;
            holder.bt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder info=new AlertDialog.Builder(getContext());
                    //info.setTitle("  ");
                    info.setMessage("是否删除");
                    info.setNegativeButton("确定", new DialogInterface.OnClickListener() {
                        public void onClick(
                                DialogInterface dialoginterface, int i) {
                            //int mListPos = info.position;
                            HashMap<String, Object> map = listData
                                    .get(position);
                            int id = Integer.valueOf((map.get("id")
                                    .toString()));
                                activity.delete(id,position);
                        }
                    });
                    info.setPositiveButton("取消", null);
                    info.show();
                }
            });
            return myView;
        }
        public final class ViewHolder {
            public TextView title;
            public TextView text;
            public ImageButton bt;
            public TextView date;
        }
    }

}
