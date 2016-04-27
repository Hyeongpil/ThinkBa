package com.example.kwan.thinkba;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.example.kwan.thinkba.Daum_map.GpsInfo;
import com.example.kwan.thinkba.Daum_map.Item;

import net.daum.mf.map.api.CameraUpdateFactory;
import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapPointBounds;
import net.daum.mf.map.api.MapView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, MapView.MapViewEventListener, MapView.POIItemEventListener {
    private GpsInfo gps;
    MapView mapView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        naviDrawerInit();
        daumMapInit();
        gps();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawerLayout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_myinfo) {
        } else if (id == R.id.nav_archive) {

        } else if (id == R.id.nav_setting) {

        } else if (id == R.id.nav_logout) {
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawerLayout);
        drawer.closeDrawers();
        return true;
    }

    private void naviDrawerInit(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawerLayout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }
    private void daumMapInit(){
        mapView = (MapView)findViewById(R.id.mapView);
        mapView.setDaumMapApiKey("965df1f1c4824c1f097710f996cc5de2");
        mapView.setMapViewEventListener(this);
        mapView.setPOIItemEventListener(this);
    }
    private void gps(){
        gps = new GpsInfo(MainActivity.this);
        if(gps.isGetLocation()){
            double latitude = gps.getLatitude();
            double longitude = gps.getLongitude();
            moveCamera(latitude,longitude);
        }
        else{
            gps.showSettingAlert();
        }
    }
    /**
     * 지도의 화면을 옮겨준다.
     * **/
    public void moveCamera(double latitude, double longitude){
        MapPointBounds mapPointBounds = new MapPointBounds();
        MapPoint mapPoint = MapPoint.mapPointWithGeoCoord(latitude,longitude);
        MapPOIItem poiItem = new MapPOIItem();
        poiItem.setMapPoint(mapPoint);
        poiItem.setSelectedMarkerType(MapPOIItem.MarkerType.CustomImage);
        poiItem.setCustomSelectedImageResourceId(R.drawable.map_pin_red);
        poiItem.setCustomImageAutoscale(false);
        poiItem.setCustomImageAnchor(0.5f, 1.0f);
        mapPointBounds.add(mapPoint);
        mapView.addPOIItem(poiItem);
        mapView.moveCamera(CameraUpdateFactory.newMapPointBounds(mapPointBounds));
    }

    @Override
    public void onMapViewInitialized(MapView mapView) {

    }

    @Override
    public void onMapViewCenterPointMoved(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewZoomLevelChanged(MapView mapView, int i) {

    }

    @Override
    public void onMapViewSingleTapped(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewDoubleTapped(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewLongPressed(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewDragStarted(MapView mapView, MapPoint mapPoint) {
    }

    @Override
    public void onMapViewDragEnded(MapView mapView, MapPoint mapPoint) {
    }

    @Override
    public void onMapViewMoveFinished(MapView mapView, MapPoint mapPoint) {
    }

    @Override
    public void onPOIItemSelected(MapView mapView, MapPOIItem mapPOIItem) {
    }

    @Override
    public void onCalloutBalloonOfPOIItemTouched(MapView mapView, MapPOIItem mapPOIItem) {
    }

    @Override
    public void onCalloutBalloonOfPOIItemTouched(MapView mapView, MapPOIItem mapPOIItem, MapPOIItem.CalloutBalloonButtonType calloutBalloonButtonType) {
    }

    @Override
    public void onDraggablePOIItemMoved(MapView mapView, MapPOIItem mapPOIItem, MapPoint mapPoint) {
    }
}
