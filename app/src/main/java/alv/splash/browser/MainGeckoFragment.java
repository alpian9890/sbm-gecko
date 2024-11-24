package alv.splash.browser;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.mozilla.geckoview.GeckoRuntime;
import org.mozilla.geckoview.GeckoSession;
import org.mozilla.geckoview.GeckoView;
import org.mozilla.geckoview.WebResponse;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MainGeckoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MainGeckoFragment extends Fragment implements FragmentCommunication{

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private GeckoView geckoView;
    private GeckoSession myGeckoSession;
    private GeckoRuntime geckoRuntime;

    public MainGeckoFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MainGeckoFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MainGeckoFragment newInstance(String param1, String param2) {
        MainGeckoFragment fragment = new MainGeckoFragment();
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
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View geckoContainer = inflater.inflate(R.layout.fragment_main_gecko, container, false);
        geckoView = geckoContainer.findViewById(R.id.mainGeckoView);

        setupGeckoView();

        return geckoContainer;
    }

    private void setupGeckoView() {


        // Inisialisasi GeckoSession
        myGeckoSession = new GeckoSession();

        myGeckoSession.setContentDelegate(
                new GeckoSession.ContentDelegate() {
                    @Override
                    public void onTitleChange(@NonNull GeckoSession session, @Nullable String title) {
                        GeckoSession.ContentDelegate.super.onTitleChange(session, title);
                    }

                    @Override
                    public void onExternalResponse(@NonNull GeckoSession session, WebResponse response) {
                        GeckoSession.ContentDelegate.super.onExternalResponse(session, response);

                    }

                    @Override
                    public void onFullScreen(@NonNull GeckoSession session, boolean fullscreen) {
                        GeckoSession.ContentDelegate.super.onFullScreen(session,fullscreen);

                    }
                }
        );
        // Inisialisasi GeckoRuntime
        if (geckoRuntime == null) {
            geckoRuntime = GeckoRuntime.create(requireContext());
        }

        myGeckoSession.open(geckoRuntime);

        // Set GeckoSession ke GeckoView
        geckoView.setSession(myGeckoSession);

        // Memuat URL
        myGeckoSession.loadUri("about:addons");
    }

    @Override
    public void loadUrlInGeckoView(String url) {
        if (myGeckoSession != null) {
            // Format URL untuk pencarian Google jika bukan URL langsung
            if (!url.startsWith("http://") && !url.startsWith("https://")) {
                url = "https://www.google.com/search?q=" + url;
            }
            myGeckoSession.loadUri(url);
        }
    }

}