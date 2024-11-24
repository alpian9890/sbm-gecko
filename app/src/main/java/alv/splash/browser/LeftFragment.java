package alv.splash.browser;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.mozilla.geckoview.AllowOrDeny;
import org.mozilla.geckoview.GeckoResult;
import org.mozilla.geckoview.GeckoRuntime;
import org.mozilla.geckoview.GeckoRuntimeSettings;
import org.mozilla.geckoview.GeckoSession;
import org.mozilla.geckoview.GeckoView;
import org.mozilla.geckoview.WebExtension;
import org.mozilla.geckoview.WebExtensionController;

import java.util.Arrays;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LeftFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LeftFragment extends Fragment implements FragmentCommunication{

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";



    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public LeftFragment() {
        // Required empty public constructor
    }

    GeckoView lGeckoView;
    GeckoRuntime lRuntime;
    GeckoSession lGeckoSession;
    WebExtension webExtension;
    WebExtensionController webExtensionController;


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instanc
     *     GeckoView lGeckoView;
     *     GeckoRuntime lRuntime;
     *     GeckoSession lGeckoSession;e of fragment LeftFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LeftFragment newInstance(String param1, String param2) {
        LeftFragment fragment = new LeftFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }


    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        // Register fragment sebagai komunikator ke MainActivity
        if (context instanceof MainActivity) {
            ((MainActivity) context).setFragmentCommunication(this);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View leftVP = inflater.inflate(R.layout.fragment_left, container, false);

        // Inisialisasi GeckoView
        lGeckoView = leftVP.findViewById(R.id.leftGeckoView);
        lGeckoSession = new GeckoSession();
        // Workaround for Bug 1758212
        lGeckoSession.setContentDelegate(new GeckoSession.ContentDelegate() {});

        if (lRuntime == null) {
            GeckoRuntimeSettings.Builder runtimeSettings = new GeckoRuntimeSettings.Builder()
                    .aboutConfigEnabled(true)
                    .remoteDebuggingEnabled(true)
                    .extensionsWebAPIEnabled(true)
                    .allowInsecureConnections(GeckoRuntimeSettings.ALLOW_ALL)
                    .extensionsProcessEnabled(true)
                    .javaScriptEnabled(true)
                    .loginAutofillEnabled(true)
                    .translationsOfferPopup(true)

                    .arguments(Arrays.asList(
                            // Add-ons related preferences
                            "-pref", "extensions.webextensions.enabled=true",
                            "-pref", "xpinstall.signatures.required=false",
                            "-pref", "extensions.langpacks.signatures.required=false",
                            "-pref", "extensions.allow-non-mpc-extensions=true",
                            "-pref", "extensions.quarantined.domains=\"\"",
                            // AMO (Mozilla Add-ons) related preferences
                            "-pref", "extensions.getAddons.search.url=https://services.addons.mozilla.org/api/v4/addons/search/",
                            "-pref", "extensions.getAddons.api.url=https://services.addons.mozilla.org/api/v4/addons/",
                            "-pref", "extensions.getAddons.langpacks.url=https://services.addons.mozilla.org/api/v4/addons/language-tools/",
                            // Recommended add-ons
                            "-pref", "extensions.getAddons.recommended.url=https://services.addons.mozilla.org/api/v4/addons/recommended/"
                    ).toArray(new String[0]));

            lRuntime = GeckoRuntime.create(requireContext(), runtimeSettings.build());
        }


        // Set NavigationDelegate untuk handle URL
        lGeckoSession.setNavigationDelegate(new GeckoSession.NavigationDelegate() {
            @Nullable
            @Override
            public GeckoResult<AllowOrDeny> onLoadRequest(@NonNull GeckoSession session,
                                                          @NonNull LoadRequest request) {
                // Allow about: URLs
                if (request.uri.startsWith("about:")) {
                    return GeckoResult.fromValue(AllowOrDeny.ALLOW);
                }
                return GeckoResult.fromValue(AllowOrDeny.ALLOW);
            }
        });

        // Set ContentDelegate untuk handle perubahan konten
        lGeckoSession.setContentDelegate(new GeckoSession.ContentDelegate() {

            public void onLocationChange(@NonNull GeckoSession session,
                                         @Nullable String url,
                                         @NonNull List<GeckoSession.PermissionDelegate.ContentPermission> perms) {
                // Handle URL changes
            }
        });

        lGeckoSession.open(lRuntime);
        lGeckoView.setSession(lGeckoSession);
        lGeckoSession.loadUri("about:addons"); // URL default saat pertama kali dibuka
        return leftVP;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        // Hapus reference komunikator saat fragment di-detach
        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).setFragmentCommunication(null);
        }

        // Cleanup GeckoView resources
        if (lGeckoSession != null) {
            lGeckoSession.close();
        }
    }

    @Override
    public void loadUrlInGeckoView(String url) {
        if (lGeckoSession != null) {
            // Khusus untuk URL about:
            if (url.startsWith("about:")) {
                lGeckoSession.loadUri(url);
                return;
            }

            // Format URL untuk pencarian Google jika bukan URL langsung
            if (!url.startsWith("http://") && !url.startsWith("https://")) {
                url = "https://www.google.com/search?q=" + Uri.encode(url);
            }
            lGeckoSession.loadUri(url);
        }
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        // Cleanup tambahan jika diperlukan
        if (lGeckoSession != null) {
            lGeckoSession.close();
        }
    }



}