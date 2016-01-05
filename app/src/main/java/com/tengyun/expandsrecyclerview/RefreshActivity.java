package com.tengyun.expandsrecyclerview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/1/5.
 */
public class RefreshActivity extends AppCompatActivity {

    private PullRefreshRecycler mPullRefresh;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refresh);

        //模拟数据
        //模拟数据
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

        PullRefreshRecycler pull = (PullRefreshRecycler) findViewById(R.id.refresh_view);
        RecyclerView recyclerView = pull.getRefreshableView();
        pull.setMode(PullToRefreshBase.Mode.BOTH);
        recyclerView.setAdapter(new TreeAdapter(this, list));

    }
}
