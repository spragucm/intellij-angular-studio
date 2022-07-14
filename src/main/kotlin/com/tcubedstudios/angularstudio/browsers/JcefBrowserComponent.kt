package com.tcubedstudios.angularstudio.browsers

import com.intellij.openapi.Disposable
import com.intellij.openapi.util.Disposer
import com.intellij.ui.jcef.JBCefBrowser
import com.tcubedstudios.angularstudio.shared.Constants.BLANK_PAGE_URL
import org.cef.browser.CefBrowser
import org.cef.browser.CefFrame
import org.cef.handler.CefDisplayHandlerAdapter
import org.cef.handler.CefLifeSpanHandlerAdapter
import org.cef.handler.CefLoadHandler
import org.cef.handler.CefLoadHandlerAdapter
import javax.swing.JComponent
import kotlin.random.Random

class JcefBrowserComponent: IBrowserComponent, Disposable {

    override val type = BrowserComponentType.JCEF

    private val browser = JBCefBrowser(BLANK_PAGE_URL)
    private val cefBrowser = browser.cefBrowser
    private val cefClient = browser.jbCefClient.cefClient
    private val jbCefClient = browser.jbCefClient

    override val component: JComponent
        get() = browser.component

    override val canGoBack: Boolean
        get() = cefBrowser.canGoBack()

    override val canGoForward: Boolean
        get() = cefBrowser.canGoForward()

    override var onUrlChangedCallback: ((String) -> Unit)? = null
    override var onProgressChangedCallback: ((Double) -> Unit)? = null

    private val cefLifeSpanHandlerAdapter = object : CefLifeSpanHandlerAdapter() {
        override fun onBeforePopup(
            browser: CefBrowser,
            frame: CefFrame,
            targetUrl: String,
            targetFrameName: String
        ): Boolean {
            load(targetUrl)
            return true
        }
    }

    private val cefDisplayHandlerAdapter = object : CefDisplayHandlerAdapter() {
        override fun onAddressChange(cefBrowser: CefBrowser, cefFrame: CefFrame, url: String) {
            onUrlChangedCallback?.invoke(url)
        }
    }

    private val cefLoadHandlerAdapter = object : CefLoadHandlerAdapter() {
        @Volatile private var progress = 0.0

        override fun onLoadingStateChange(cefBrowser: CefBrowser, isLoading: Boolean, canGoBack: Boolean, canGoForward: Boolean) {
            super.onLoadingStateChange(cefBrowser, isLoading, canGoBack, canGoForward)
            progress = when {
                isLoading && progress == 0.0 -> Random.nextDouble(0.1, 0.8)
                !isLoading -> 0.0
                else -> progress
            }
            onProgressChangedCallback?.invoke(progress)
        }

        override fun onLoadEnd(browser: CefBrowser, frame: CefFrame, httpStatusCode: Int) {
            super.onLoadEnd(browser, frame, httpStatusCode)
            progress = 0.0
            onProgressChangedCallback?.invoke(progress)
        }

        override fun onLoadError(browser: CefBrowser, frame: CefFrame, errorCode: CefLoadHandler.ErrorCode, errorText: String, failedUrl: String) {
            super.onLoadError(browser, frame, errorCode, errorText, failedUrl)
            progress = 0.0
            onProgressChangedCallback?.invoke(progress)
        }
    }

    init {
        jbCefClient.addLifeSpanHandler(cefLifeSpanHandlerAdapter, cefBrowser)
        jbCefClient.addDisplayHandler(cefDisplayHandlerAdapter, cefBrowser)
        jbCefClient.addLoadHandler(cefLoadHandlerAdapter, cefBrowser)
    }

    override fun load(url: String) = browser.loadURL(url)

    override fun back() {
        if (canGoBack) cefBrowser.goBack()
    }

    override fun forward() {
        if (canGoForward) cefBrowser.goForward()
    }

    override fun openDevTools() = browser.openDevtools()

    override fun executeScript(script: String) {
        cefBrowser.executeJavaScript(script, null, 0)
    }

    override fun dispose() {
        cefClient.removeLoadHandler()
        cefBrowser.stopLoad()
        cefBrowser.close(false)
        Disposer.dispose(browser)
    }
}