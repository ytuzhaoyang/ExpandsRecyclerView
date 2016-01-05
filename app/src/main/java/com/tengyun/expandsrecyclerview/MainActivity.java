package com.tengyun.expandsrecyclerview;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private RecyclerView mRecyclerView;
    private TreeAdapter mAdapter;
    private TreeAdapter.TreeViewHolder treeViewHolder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        FrameLayout frameLayout = (FrameLayout) findViewById(R.id.main_layout);

        List<Tree<?>> list = new ArrayList<>();

        List<Tree<Tree<?>>> treeList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            //Tree<Tree<?>> treeTree = new Tree<Tree<?>>(new Tree<>(String.format("第%02d组",i)));
            Tree<String> treeTree = new Tree<>(String.format("第%02d组", i));
            for (int j = 0; j < 10; j++) {
                Tree<String> cTree = new Tree<>(String.format("第%02d组 第%02d条", i, j));
                treeTree.addChild(cTree);
                for (int z = 0; z < 10; z++) {
                    Tree<String> dTree = new Tree<>(String.format("第%02d组 第%02d条 第%02d个", i, j, z));
                    cTree.addChild(dTree);
                }
            }
            list.add(treeTree);
        }

//        //模拟数据
//        List<Tree<?>> list = new ArrayList<>();
//        for (int i = 0; i < 10; i++) {
//            Tree<String> tree = new Tree<>(String.format("第%02d组",i));
//            for (int j = 0; j < 10; j++) {
//                tree.addChild(new Tree<>(String.format("第%02d组 第%02d条",i,j)));
//            }
//            list.add(tree);
//        }
        mAdapter = new TreeAdapter(this, list);
        mRecyclerView.setAdapter(mAdapter);

        treeViewHolder = mAdapter.onCreateViewHolder(mRecyclerView, 0);
        treeViewHolder.itemView.setVisibility(View.GONE);
        treeViewHolder.itemView.setOnClickListener(this);
        frameLayout.addView(treeViewHolder.itemView);

        mRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                View child = mRecyclerView.getChildAt(0); //屏幕上第一个被看见的条目
                //屏幕上第一个被看见的条目对应的position
                int position = mRecyclerView.getChildAdapterPosition(child);
                for (int i = position; i >= 0; i--) {
                    if (mAdapter.getItemViewType(i) == 0) { //第一个为根节点的数据
                        treeViewHolder.itemView.setVisibility(View.VISIBLE);
                        mAdapter.onBindViewHolder(treeViewHolder, i);
                        break;
                    }
                }
                for (int i = 1; i < mRecyclerView.getChildCount(); i++) {
                    if (ViewCompat.getY(mRecyclerView.getChildAt(i)) > treeViewHolder.itemView.getHeight()) {
                        ViewCompat.setTranslationY(treeViewHolder.itemView, 0);
                        break;
                    }
                    if (mAdapter.getItemViewType(position + i) == 0) {
                        View view = treeViewHolder.itemView;
                        ViewCompat.setTranslationY(view, ViewCompat.getY(mRecyclerView.getChildAt(i)) - view.getHeight());
                        break;
                    }

                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        startActivity(new Intent(MainActivity.this, RefreshActivity.class ));
    }
}
