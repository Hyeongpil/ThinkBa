package com.hyeongpil.thinkba.navigation;

import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.example.games.basegameutils.BaseGameActivity;
import com.hyeongpil.thinkba.R;
import com.hyeongpil.thinkba.model.ArchiveModel;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by hp on 2016. 7. 18..
 */
public class ArchiveActivity extends BaseGameActivity{

    private ArrayList<ArchiveModel> archiveList;
    private Archive_Adapter adapter;

    @Bind(R.id.archive_recycler_view)
    RecyclerView recyclerView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_archive);
        ButterKnife.bind(this);

        init();
        initCollapsingToolbar();

        preparedArchive();

    }

    private void init(){
        archiveList = new ArrayList<>();
        adapter = new Archive_Adapter(this,archiveList);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this,2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        try {
            Glide.with(this).load(R.drawable.archive_photo).into((ImageView) findViewById(R.id.archive_top_img));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void preparedArchive(){
        // TODO: 2016. 7. 19. 업적 취득 값 DB 연동하기
        ArchiveModel archives = new ArchiveModel("띵바 피플","띵바 앱 첫 실행",R.drawable.archive_test,false);
        archiveList.add(archives);

        archives = new ArchiveModel("목표를 포착했다","길 찾기 5회",R.drawable.archive_test,false);
        archiveList.add(archives);

        archives = new ArchiveModel("띵바를 부리는 자","띵바 앱 50회 실행",R.drawable.archive_test,false);
        archiveList.add(archives);

        archives = new ArchiveModel("영웅은 죽지 않아요","병원 주변 검색",R.drawable.archive_test,false);
        archiveList.add(archives);

        archives = new ArchiveModel("미워도 다시한번","앱 로그아웃",R.drawable.archive_test,false);
        archiveList.add(archives);

        archives = new ArchiveModel("Don`t starve","편의점 주변 검색",R.drawable.archive_test,false);
        archiveList.add(archives);

        adapter.notifyDataSetChanged();
    }




    /**
     * Initializing collapsing toolbar
     * Will show and hide the toolbar title on scroll
     */
    private void initCollapsingToolbar() {
        final CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle(" ");
        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.appbar);
        appBarLayout.setExpanded(true);

        // hiding & showing the title when toolbar expanded & collapsed
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    collapsingToolbar.setTitle(getString(R.string.app_name));
                    isShow = true;
                } else if (isShow) {
                    collapsingToolbar.setTitle(" ");
                    isShow = false;
                }
            }
        });
    }


    /**
     * RecyclerView item decoration - give equal margin around grid item
     */
    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
    }
    /**
     * Converting dp to pixel
     */
    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }
    @Override
    public void onSignInFailed() {

    }

    @Override
    public void onSignInSucceeded() {

    }


}
