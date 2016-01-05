package com.tengyun.expandsrecyclerview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Collection;
import java.util.List;

/**
 * Created by Administrator on 2016/1/5.
 */
public class TreeAdapter extends RecyclerView.Adapter<TreeAdapter.TreeViewHolder> implements View.OnClickListener {

    private Context context;
    private List<Tree<?>> list;
    private RecyclerView recyclerView;
    private MyItemAnimator mItemAnimator;

    public TreeAdapter(Context context, List<Tree<?>> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public TreeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        switch (viewType){
            case 0: //根节点
                view = LayoutInflater.from(context).inflate(R.layout.group_item, parent, false);
                break;
            case 1: //第二层节点
                view = LayoutInflater.from(context).inflate(R.layout.group_item2, parent, false);
                break;
            case 2: //叶节点
                view = LayoutInflater.from(context).inflate(R.layout.child_item, parent, false);
                break;
            default: //默认节点
                view = LayoutInflater.from(context).inflate(R.layout.child_item, parent, false);
                break;
        }

        // 设置点击事件
        view.setOnClickListener(this);
        return new TreeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TreeViewHolder holder, int position) {
        Tree<?> tree = list.get(position);
        switch (tree.getLevel()) {
            case 0:
            case 1:
                holder.group_text.setText((String)tree.getData());
                holder.expand.setChecked(tree.isExpand());
                break;
            case 2:
                holder.child_text.setText((String) tree.getData());
                break;
        }
        holder.position = position;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    /**
     * 返回条目的类型，该返回值对应onCreateViewHolder（)方法中第二个参数值
     * @param position
     * @return
     */
    @Override
    public int getItemViewType(int position) {
        return list.get(position).getLevel();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        this.recyclerView = recyclerView;
        mItemAnimator = new MyItemAnimator();
        //设置自定义的动画
        recyclerView.setItemAnimator(mItemAnimator);
    }

    /**
     * 点击事件的回调
     * @param v
     */
    @Override
    public void onClick(View v) {
        // 得到点击的位置
        int position = recyclerView.getChildAdapterPosition(v);
        //根据位置获取点击的条目对象
        Tree<?> tree = list.get(position);
        if (tree.isExpandable()){ //可以被展开的(可能是根节点也可能是二级节点)
            if (tree.getLevel() == 0){ //根节点
                if (tree.isExpand()) { //已经被展开，点击关闭（移出所有的孩子）
                    //得到点击对应节点下面的所有二级节点的集合
                    List<Tree<?>> chlidren = tree.getChlidren();
                    //遍历根节点下面的所有二级节点
                    for (int i = 0; i < chlidren.size(); i++) {
                        Tree<?> t = chlidren.get(i);
                        //判断二级节点是否是展开状态
                        if (t.isExpand()){
                            //二级节点是展开状态，先关闭二级节点（移除该节点下的所有孩子）
                            removeAll(position+1, t.getChlidren());
                        }
                        // 为了记住二级节点的状态，不进行状态的修改
                        notifyItemChanged(position+1);
                    }
                    removeAll(position + 1, tree.getChlidren());
                } else { //根节点关闭状态，点击展开（显示所有的孩子）
                    addAll(position + 1, tree.getChlidren());
                    //得到点击对应节点下面的所有二级节点的集合
                    List<Tree<?>> chlidren = tree.getChlidren();
                    //遍历根节点下面的所有二级节点
                    for (int i = 0; i < chlidren.size(); i++) {
                        Tree<?> t = chlidren.get(i);
                        //判断二级节点是否是展开状态
                        if (t.isExpand()){
                            //二级节点是展开状态，展开二级节点（添加该节点下的所有孩子）
                            addAll(position + i + 1 + 1, t.getChlidren());
                        }
                        notifyItemChanged(position+i+1);
                    }
                }
            }else {//点击的是二级节点
                if (tree.isExpand()) { //已经被展开，点击关闭（移出所有的孩子）
                    removeAll(position + 1, tree.getChlidren());
                } else {//关闭状态，点击展开（显示所有的孩子）
                    addAll(position + 1, tree.getChlidren());
                }
            }
            //修改节点的状态
            tree.setExpand(!tree.isExpand());
            notifyItemChanged(position);
        }else {//叶节点（不可以被展开）
            Toast.makeText(context, (String)(tree.getData()), Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 添加所有，用于列出父节点下的所有子节点
     * @param position
     * @param collection
     */
    public void addAll(int position , Collection<? extends Tree<?>> collection){
        list.addAll(position,collection);
        notifyItemRangeInserted(position, collection.size());
    }

    /**
     * 移除所有节点，用于关闭展开时，隐藏所有的子节点
     * @param position
     * @param collection
     */
    public void removeAll(int position , Collection<? extends Tree<?>> collection){
        list.removeAll(collection);
        notifyItemRangeRemoved(position, collection.size());
    }

    public static class TreeViewHolder extends RecyclerView.ViewHolder{
        private CheckBox expand;
        private TextView group_text;
        private TextView child_text;
        private int position;

        public int getMyPosition() {
            return position;
        }
        public TreeViewHolder(View itemView) {
            super(itemView);
            expand = (CheckBox) itemView.findViewById(R.id.expand);
             group_text = (TextView) itemView.findViewById(R.id.group_text);
             child_text = (TextView) itemView.findViewById(R.id.child_text);
        }
    }
}
