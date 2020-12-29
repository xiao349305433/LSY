package met.hx.com.base.base.webview;

import android.webkit.WebChromeClient;
import android.webkit.WebView;

import androidx.annotation.CallSuper;

public class BaseWebChromeClient extends WebChromeClient {
        @CallSuper
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
        }

        @CallSuper
        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
        }

        public BaseWebChromeClient() {
        }
    }