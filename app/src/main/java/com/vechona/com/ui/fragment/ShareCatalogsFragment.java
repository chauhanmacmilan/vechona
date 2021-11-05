package com.vechona.com.ui.fragment;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.vechona.app.BuildConfig;
import com.vechona.app.R;
import com.vechona.com.data.local.PreferenceDataHelper;
import com.vechona.com.data.remote.ApiService;
import com.vechona.com.data.remote.RemoteServiceHelper;
import com.vechona.com.ui.activity.CatalogActivity;
import com.vechona.com.ui.adapter.CatalogListAdapter;
import com.vechona.com.ui.interfaces.OnItemClickListener;
import com.vechona.com.ui.model.Catalog;
import com.vechona.com.ui.utils.ImageDownloadUtils;
import com.vechona.com.ui.utils.ShareCatalogUtils;
import com.vechona.com.ui.utils.ViewUtils;
import com.vechona.com.ui.utils.WhatsAppShareUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ShareCatalogsFragment extends Fragment {

    private static final String IS_WISH_LIST = "is_wish_list";
    private static final int PERMISSION_REQUEST_WRITE_DATA = 2;

    @BindView(R.id.rvSharedCatalog)
    RecyclerView rvSharedCatalog;
    @BindView(R.id.tvEmptyShareCatalog)
    TextView tvEmpty;

    private CatalogListAdapter catalogListAdapter;
    private PreferenceDataHelper preferenceDataHelper;
    private List<Catalog> catalogList = new ArrayList<>();
    private boolean isWhatsappShare, isWishList;
    private int catalogId1;
    private ApiService apiService;
    private int margin1;

    public static ShareCatalogsFragment newInstance(boolean isWishList) {
        Bundle args = new Bundle();
        args.putBoolean(IS_WISH_LIST, isWishList);
        ShareCatalogsFragment fragment = new ShareCatalogsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && preferenceDataHelper != null)
            prepareCatalogList();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_shared_catalog, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
        initData();
    }

    private void initView() {
        catalogListAdapter = new CatalogListAdapter(new OnItemClickListener() {
            @Override
            public void onClickAtPosition(View view, int catalogId) {
                CatalogActivity.start(getContext(), getCatalogById(catalogId));
            }

            @Override
            public void onClickWishList(View view, int catalogId) {
                Catalog catalog = getCatalogById(catalogId);
                if (!preferenceDataHelper.isWishOrShareList(catalog, true))
                    ShareCatalogUtils.addWishOrSharedCatalog(apiService, getContext(), catalog, true);
                else {
                    ShareCatalogUtils.deleteFromWishListAPI(apiService, getContext(), catalog);
                    if (isWishList)
                        catalogListAdapter.deleteItem(catalogList.indexOf(catalog));
                    if (catalogList.size() == 0) tvEmpty.setVisibility(View.VISIBLE);
                    else tvEmpty.setVisibility(View.GONE);
                }
            }

            @Override
            public void onClickShareNow(View view, final int catalogId) {
                catalogId1 = catalogId;
                showSetMarginDialog();
            }
        });

        rvSharedCatalog.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        rvSharedCatalog.setAdapter(catalogListAdapter);
    }

    private void showSetMarginDialog() {
        String tag = "SetMarginDialog";
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        Fragment prev = getFragmentManager().findFragmentByTag(tag);
        if (prev != null)
            return;
        SetMarginDialog dialog = SetMarginDialog.newInstance();
        dialog.setMarginListener(new SetMarginDialog.SetMarginDialogListener() {
            @Override
            public void onSetMarginDialogSubmitClick(int margin) {
                margin1 = margin;
                shareWhatsappImage();
            }
        });
        dialog.show(fragmentTransaction, tag);
    }

    private void shareWhatsappImage() {
        if (WhatsAppShareUtils.isWhatsAppInstalled(getContext())) {
            Catalog catalog = getCatalogById(catalogId1);
            isWhatsappShare = true;
            if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                checkRuntimePermission(PERMISSION_REQUEST_WRITE_DATA);
            } else {
                ImageDownloadUtils.shareImages(getContext(), catalog, null);
                if (!preferenceDataHelper.isWishOrShareList(catalog, false))
                    ShareCatalogUtils.addWishOrSharedCatalog(apiService, getContext(), catalog, false);
            }
        } else
            ViewUtils.showToast(getContext(), getString(R.string.whatsapp_not_installed));
    }

    private void checkRuntimePermission(final int request_id) {
        if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            new AlertDialog.Builder(getContext())
                    .setTitle(R.string.dialog_title)
                    .setMessage(R.string.denied_permission)
                    .setCancelable(false)
                    .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, request_id);
                        }
                    }).show();

        } else {
            if (preferenceDataHelper.isFirstTimeAskingPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                preferenceDataHelper.firstTimeAskingPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, false);
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, request_id);
            } else {
                new AlertDialog.Builder(getContext())
                        .setTitle(R.string.dialog_title)
                        .setMessage(R.string.disable_permission)
                        .setCancelable(false)
                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                startActivity(new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:" + BuildConfig.APPLICATION_ID)));
                            }
                        }).show();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {

            case PERMISSION_REQUEST_WRITE_DATA: {
                if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    Catalog catalog = getCatalogById(catalogId1);
                    ImageDownloadUtils.shareImages(getContext(), catalog, null);
                    if (!preferenceDataHelper.isWishOrShareList(catalog, false)) {
                        ShareCatalogUtils.addWishOrSharedCatalog(apiService, getContext(), catalog, false);
                    }
                }
                break;
            }
        }
    }

    private void initData() {
        preferenceDataHelper = PreferenceDataHelper.getInstance(getContext());
        apiService = RemoteServiceHelper.createService(ApiService.class,
                RemoteServiceHelper.getRetrofitInstance(getContext()));
        if (getArguments() != null)
            isWishList = getArguments().getBoolean(IS_WISH_LIST, false);
        prepareCatalogList();
    }

    private void prepareCatalogList() {
        if (isWishList) catalogList = preferenceDataHelper.getFavorite();
        else catalogList = preferenceDataHelper.getSharedItem();
        setCatalogList();
    }

    private void setCatalogList() {
        if (catalogList != null) {
            if (catalogList.size() > 0) {
                catalogListAdapter.setCatalogList(catalogList);
                tvEmpty.setVisibility(View.GONE);
            } else {
                catalogListAdapter.setCatalogList(catalogList);
                tvEmpty.setVisibility(View.VISIBLE);
            }
        } else tvEmpty.setVisibility(View.VISIBLE);
    }

    private Catalog getCatalogById(int catalogId) {
        for (Catalog catalog : catalogList) {
            if (catalog.getCatalogId() == catalogId)
                return catalog;
        }
        return null;
    }

    @Override
    public void onResume() {
        super.onResume();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (isWhatsappShare) {
                    isWhatsappShare = false;
                    int price = getCatalogById(catalogId1).getCatalogBasePrice() + margin1;
                    String description = getString(R.string.title_cart_price) + " : " + price +
                            "\n" + getCatalogById(catalogId1).getCatalogDescription();
                    WhatsAppShareUtils.shareText(description, getContext());
                }
            }
        }, 600);
    }

}
