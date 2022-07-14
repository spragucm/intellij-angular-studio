package com.tcubedstudios.angularstudio.shared.libs.shopobot

import com.tcubedstudios.angularstudio.shared.libs.shopobot.FragmentEncoder.decode
import com.tcubedstudios.angularstudio.shared.libs.shopobot.FragmentEncoder.encode
import java.io.UnsupportedEncodingException
import java.lang.IllegalArgumentException
import java.lang.NullPointerException
import java.net.*
import java.util.*

// Pulled from:
//https://www.javatips.net/api/compliance-master/ctk-transport/src/main/java/org/ga4gh/ctk/transport/URL.java
/**
 * This class represents a URL for an internet resource. It is not to be
 * confused with a URI. I wrote this class to make a URL class for Java that is
 * more OO than the traditional java.net.URL. It is most useful for modifying
 * URLs for a web application.
 *
 * @author [julius schorzman](https://github.com/juliuss)
 */
class URL(url: String?) : Iterable<URL.Parameter?> {
    /**
     * Represents a URL parameter. Names and values are automatically encoded on
     * entry and decoded on removal.
     */
    inner class Parameter {
        private var name = ""
        private var value = ""

        /**
         * Creates a parameter with the provided name and an empty value. The name
         * is encoded upon storage.
         *
         * @param name
         * The name of the parameter. Silently accepts null and empty
         * strings, however a blank parameter will be returned when
         * toString() is called.
         */
        constructor(name: String?) {
            this.name = name?.let { urlEncode(it) } ?: ""
        }

        /**
         * Creates a parameter with the provided name and value, both of which are
         * encoded upon storage.
         *
         * @param name
         * The name of the parameter. Silently accepts null and empty
         * strings, however a blank parameter will be returned when
         * toString() is called.
         * @param value
         * The value of the parameter. Accepts null and empty strings.
         */
        constructor(name: String?, value: String?) {
            this.name = name?.let { urlEncode(it) } ?: ""
            this.value = value?.let { urlEncode(it) } ?: ""
        }

        /**
         * This private constructor is used only when parsing existing urls
         */
        constructor(name: String?, value: String?, noEncode: Boolean) {
            this.name = name ?: ""
            this.value = value ?: ""
        }

        /**
         * Returns the name of this parameter. It is automatically decoded.
         */
        fun getName(): String {
            return urlDecode(name)
        }

        /**
         * Returns the value of this parameter. It is automatically decoded.
         */
        fun getValue(): String {
            return urlDecode(value)
        }

        /**
         * Returns an encoded string representation of this parameter, usually
         * name=value If the name is empty an empty string is returned. If the value
         * is empty, the name alone is returned.
         */
        override fun toString(): String {
            if (name.isEmpty()) {
                return ""
            }
            return if (value.isEmpty()) {
                name
            } else "$name=$value"
        }
    }

    /**
     * This enum represents supported Protocols and their default ports.
     */
    enum class Protocol(
        /**
         * Returns the default port for this protocol. Example: http returns 80.
         */
        val defaultPort: Int
    ) {
        // default ports
        http(80), https(443), ftp(21);

    }

    /**
     * Returns the protocol portion of this URL. For example, the enum for http is
     * returned from: http://www.google.com/
     *
     * For reference the get methods are named as:
     * protocol://username:password@host:port/path?query#fragment
     *
     * @protocol A Protocol enum representing this urls protocol (or scheme).
     * Never null.
     */
    var protocol = Protocol.http
        protected set

    private var _username = ""

    private var _password = ""

    /**
     * Returns the host portion of this URL. For example, "www.google.com" from:
     * http://www.google.com/?name=value#fragment
     *
     * For reference the get methods are named as:
     * protocol://username:password@host:port/path?query#fragment
     */
    var host = ""
        protected set

    /**
     * Returns the port of this URL. If no port is present in the URL, the default
     * for the scheme is provided. For example, 80 from:
     * http://www.google.com/?name=value#fragment
     *
     * and 8080 from: http://www.google.com:8080/?name=value#fragment
     *
     * For reference the get methods are named as:
     * protocol://username:password@host:port/path?query#fragment
     *
     * @return The port of this url. If no other port is provided, the protocol's
     * default port will be returned. (Example: 80 for http)
     */
    var port = protocol.defaultPort
        protected set

    /**
     * Returns the path portion of this URL. For example,
     * "/directory/to/resource.html" from:
     * http://www.google.com/directory/to/resource.html?name=value#fragment
     *
     * For reference the get methods are named as:
     * protocol://username:password@host:port/path?query#fragment
     *
     * @return The path of this url. The path will always be at minimum "/" and
     * will never be empty.
     */
    var path: String? = "/"
        protected set
    protected var parameters: MutableList<Parameter> = ArrayList()
    private var _fragment: String? = ""

    /**
     * Creates a URL given a provided URL. Reference URL(String url) for more
     * info.
     */
    constructor(uri: URI) : this(uri.toString()) {}

    /**
     * Creates a URL given a provided URL. Reference URL(String url) for more
     * info.
     */
    constructor(url: java.net.URL) : this(url.toExternalForm()) {}

    /**
     * Creates a URL given a provided string. Must be a valid URL (according to
     * java.net.URI). **Note that empty, null, or invalid URLs will throw a
     * RuntimeException** (either NullPointerException for a null parameter or
     * IllegalArgumentException for an empty or invalid URL), so the expectation
     * is that the calling method will check the url for correctness. To help with
     * this, you can use the static method:
     *
     * <pre>
     * `isValid(String url);`
    </pre> *
     *
     * or the factory-like:
     *
     * <pre>
     * `URL.get("an invalid url!")`
    </pre> *
     *
     * which will not throw an exception and instead return null, or:
     *
     * <pre>
     * `URL.get("an invalid url!","http://www.example.com/a-default-url.html")`
    </pre> *
     *
     * which will not throw an exception and instead return the default url
     * provided.
     *
     * @param url
     * The external form of the URL. Ex: http://www.google.com If this
     * parameter is empty or invalid an IllegalArgumentException is
     * thrown. If this parameter is null a NullPointerException.
     * @throws IllegalArgumentException
     * Thrown if url is not valid.
     * @throws NullPointerException
     * Thrown if parameter is null.
     */
    init {

        // test the input
        var url = url ?: throw NullPointerException("URL cannot be null")
        require(!url.isEmpty()) { "URL cannot be empty" }

        // if the url does not contain a scheme/protocol, default to http
        // this allows a constructor to be called such as "www.example.com"
        if (!url.contains("://")) {
            url = Protocol.http.toString() + "://" + url
        }
        val u: URI
        try {
            u = URI(url)
            requireNotNull(u.host) { "The class can only represent URLs." }
        } catch (e: URISyntaxException) {
            throw IllegalArgumentException(e)
        }
        protocol = Protocol.valueOf(u.scheme)
        _username = ""
        _password = ""
        host = u.host
        if (host == null) {
            host = ""
        }
        port = u.port
        if (port < 1) {
            port = protocol.defaultPort
        }
        path = u.rawPath
        if (path == null || path!!.isEmpty()) {
            path = "/"
        }
        _fragment = u.rawFragment
        if (_fragment == null) {
            _fragment = ""
        }

        // query string
        parseQueryString(url)

        // parse user info
        val info = u.userInfo
        if (info != null) {
            val pos = info.indexOf(':')
            if (pos >= 0 && pos + 1 < info.length) {
                _username = info.substring(0, pos)
                _password = info.substring(pos + 1)
            } else {
                _username = info
            }
        }
    }

    /**
     * Adds a parameter to the URL with the provided name and value. Reference
     * addParameter(String name, String value) for more info.
     */
    fun addParameter(name: String?, value: Int): URL {
        addParameter(name, value.toString() + "")
        return this
    }

    /**
     * Returns the user portion of this URL. For example, "user" from:
     * ftp://user:pass@ftp.example.com/?name=value#fragment
     *
     * For reference the get methods are named as:
     * protocol://username:password@host:port/path?query#fragment
     *
     * @return The URLs username, or an empty string if none. Never null.
     */
    fun getUsername(): String {
        return urlDecode(_username)
    }

    /**
     *
     * @param username
     * @return This object for chaining.
     */
    fun setUsername(username: String): URL {
        this._username = urlEncode(username)
        return this
    }

    /**
     * Returns the password portion of this URL. For example, "pass" from:
     * ftp://user:pass@ftp.example.com/?name=value#fragment
     *
     * For reference the get methods are named as:
     * protocol://username:password@host:port/path?query#fragment
     *
     * @return The password of this url or an empty string if none is present.
     * Never null.
     */
    fun getPassword(): String {
        return urlDecode(_password)
    }

    /**
     *
     * @param password
     * @return This object for chaining.
     */
    fun setPassword(password: String): URL {
        _password = urlEncode(password)
        return this
    }

    /**
     * Returns a copy of this URL.
     */
    fun clone(): URL {
        return URL(this.toString())
    }
    
    /**
     * Adds a parameter to the URL with the provided name and value. Example:
     *
     * <pre>
     * `URL ( "http://www.shopobot.com/search" ).addParameter( "query" ,
     * "ipod" ).equals( "http://www.shopobot.com/search?query=ipod" );`
    </pre> *
     *
     * **NOTE:** Adding a parameter that already exists is supported. Example:
     *
     * <pre>
     * `URL( "http://www.shopobot.com/search?query=ipod" ).addParameter(
     * "query" , "ipad" ).equals(
     * "http://www.shopobot.com/search?query=ipod&query=ipad" );
     * </pre>
     *
     * If instead you want to replace a parameter's value, use setParameter.
     *
     * name
     * The name of the parameter. It is automatically encoded if
     * necessary. If null or empty is passed silently no parameter is
     * added.
     * value
     * The value of the parameter. It is automatically encoded if
     * necessary. If null or empty, a "name only" parameter is added.`
    </pre> */
    fun addParameter(name: String?, value: String?): URL {
        if (name == null || name.isEmpty()) {
            return this
        }
        val p: Parameter = Parameter(name, value ?: "")
        parameters.add(p)
        return this
    }

    /**
     * This private method is only used when creating a url that already has
     * parameters and no encoding is necessary.
     */
    private fun addParameterNoEncode(name: String?, value: String?): URL {
        if (name == null || name.isEmpty()) {
            return this
        }
        val p: Parameter = Parameter(name, value ?: "", false)
        parameters.add(p)
        return this
    }

    /**
     * Decrements by 1 the integer value of the parameter in the URL that matches
     * the provided name. If the URL contains multiple parameters, the first
     * encountered that can be casted to an int will be decremented. If no
     * parameter exists that equals the provided name AND can be casted to an int,
     * the url will not be modified.
     *
     * @param name
     * The name of the parameter to decrement. Null and or an empty
     * string will silently return without changing the url.
     */
    fun decrementParameter(name: String?): URL {
        if (name == null || name.isEmpty()) {
            return this
        }
        for (p in parameters) {
            if (p.getName() == name) {
                try {
                    var i = p.getValue().toInt()
                    i--
                    if (i == 1) {
                        removeParameter(name)
                    } else {
                        setParameter(name, i)
                    }
                    return this
                } catch (e: NumberFormatException) {
                    // ignored, keep trying or return default
                }
            }
        }
        return this
    }

    /**
     * Compares this object with a similar object, String, java.net.URL, java.netURI.  Returns true if the external form of the URL is identical.
     */
    override fun equals(anObject: Any?): Boolean {
        if (anObject == null) {
            return false
        }
        if (this === anObject) {
            return true
        }
        if (anObject is String) {
            return toString() == anObject
        }
        if (anObject is java.net.URL) {
            return toString() == anObject.toExternalForm()
        }
        return if (anObject is URI) {
            toString() == anObject.toString()
        } else false
    }

    /**
     * Helper method that URL decodes a value.
     */
    private fun fragmentDecode(value: String?): String {
        return try {
            decode(value!!, ENCODING)
        } catch (e: UnsupportedEncodingException) {
            throw IllegalArgumentException(ENCODING + ENCODING_ERROR, e)
        }
    }

    /**
     * Helper method that URL encodes a value.
     */
    private fun fragmentEncode(value: String): String {
        return try {
            encode(value, ENCODING)
        } catch (e: UnsupportedEncodingException) {
            throw IllegalArgumentException(ENCODING + ENCODING_ERROR, e)
        }
    }

    /**
     * Returns the fragment portion of this URL. For example, "fragment" from:
     * http://www.google.com/?name=value#fragment
     *
     * For reference the get methods are named as:
     * protocol://username:password@host:port/path?query#fragment
     */
    fun getFragment(): String {
        return fragmentDecode(_fragment)
    }

    /**
     * Gets the integer value of the parameter in the URL that matches the
     * provided name. If the URL contains multiple parameters, the first
     * encountered that can be casted to an int will be returned. If no parameter
     * exists with the provided name AND cannot be casted to an int, the provided
     * defaul int value will be returned.
     *
     * @param name
     * The name of the parameter to return. The name is automatically
     * encoded if necessary.
     * @param defaul
     * If the is no parameter to return, this value is returned instead.
     * @return The value of the parameter, of defaul if none present.
     */
    fun getParameter(name: String, defaul: Int): Int {
        for (p in parameters) {
            if (p.getName() == name) {
                try {
                    return p.getValue().toInt()
                } catch (e: NumberFormatException) {
                    // ignored, keep trying or return default
                }
            }
        }
        return defaul
    }

    /**
     * Gets the value of the parameter in the URL that matches the provided name.
     * If the URL contains multiple parameters, the first encountered will be
     * returned. In cases where the URL contains multiple parameters with the same
     * name, used getParameters(String name) instead. If no parameter exists with
     * the provided name, the provided defaul will be returned. If the parameter
     * has no value, "www.example.com/?test", an empty string is returned, **not
     * the defaul value**.
     *
     * @param name
     * The name of the parameter to return. The name is automatically
     * encoded if necessary.
     * @param defaul
     * If the is no parameter to return, this value is returned instead.
     * @return The value of the parameter, of defaul if none present.
     */
    fun getParameter(name: String, defaul: String): String {
        for (p in parameters) {
            if (p.getName() == name) {
                return p.getValue()
            }
        }
        return defaul
    }

    /**
     * Gets the values of the parameters in the URL that matche the provided name.
     * If the URL contains unique parameter names, use getPatameter instead. If no
     * parameter exists with the provided name, an empty array will be returned.
     *
     * @param name
     * The name of the parameter(s) to return. The name is automatically
     * encoded if necessary.
     * @return A string array of decoded parameter values.
     */
    fun getParameters(name: String): Array<String> {
        var name = name
        name = urlEncode(name)
        val array: MutableList<String> = ArrayList()
        for (p in parameters) {
            if (p.getName() == name) {
                array.add(p.getValue())
            }
        }
        return array.toTypedArray()
    }

    /**
     * Sometimes paths are formed poorly with double slashes This regex makes
     * sure doulbe slashes are treated as a single slash.
     */
    /**
     * Returns the path of this URL split by /. For example,
     * "/directory/to/resource.html" from: returns a List<String> with three
     * elements: [0] : directory [1] : to [2] : resource.html Note that poorly
     * formed URLs, like "//directory///to////resource.html" would return the
     * exact same list as above.
     *
     * If the path is simply / an empty list is returned.
    </String> */
    val pathElements: List<String>
        get() {
            if (path!!.replace("/".toRegex(), "").isEmpty()) {
                return ArrayList()
            }
            /**
             * Sometimes paths are formed poorly with double slashes This regex makes
             * sure doulbe slashes are treated as a single slash.
             */
            val p = path!!.replace("[/]{2,}".toRegex(), "/")
            return Arrays.asList(*p.split("/".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray())
        }

    /**
     * Returns the parent directory of a directory in this URLs path. For example,
     * if this url's path is: /a/b/c/d.html all of these statements are true:
     *
     * <pre>
     * getParentDirectory("d.html").equals("c")
     * getParentDirectory("b").equals("a")
     * getParentDirectory("a").equals("")
     * getParentDirectory("not-present").equals("")
    </pre> *
     *
     * In all fault cases and empty string is returned, including: this url's path
     * is empty/root ("/"), if the provided directory or file is not found, the
     * parent category is root.
     */
    fun getParentDirectory(child: String): String {
        val elements = pathElements
        val i = elements.indexOf(child)
        return if (i < 0) {
            "" // not found
        } else if (i == 0) {
            "" // no parent
        } else {
            elements[elements.indexOf(child) - 1]
        }
    }

    /**
     * Returns the child directory (or filename) of a directory in this URLs path.
     * For example, if this url's path is: /a/b/c/d.html all of these statements
     * are true:
     *
     * <pre>
     * getParentDirectory("a").equals("b")
     * getParentDirectory("c").equals("d.html")
     * getParentDirectory("d.html").equals("")
     * getParentDirectory("not-present").equals("")
    </pre> *
     *
     * In all fault cases and empty string is returned, including: this url's path
     * is empty/root ("/"), if the provided directory or file is not found, the
     * matching element is the last element in the path.
     */
    fun getChildDirectory(parent: String): String {
        val elements = pathElements
        val i = elements.indexOf(parent)
        return if (i < 0) {
            "" // not found
        } else if (i == elements.size - 1) {
            "" // no child
        } else {
            elements[elements.indexOf(parent) + 1]
        }
    }

    /**
     * Returns a new URL given a relative path.
     *
     * For example, if this URL object represents "http://example.com" then
     * calling this method with "/a.html" will return "http://example.com/a.html"
     *
     * Warning: be careful not to pass a non-relative url.
     *
     * @param relativePath
     * @return A URL object representing the relative path.
     */
    fun resolveRelative(relativePath: String): URL {
        var relativePath = relativePath
        relativePath = relativePath.trim { it <= ' ' }
        val baseURI = toJavaURI()
        val resultURI = baseURI!!.resolve(relativePath)
        return URL(resultURI.toString())
    }

    /**
     * Increments by 1 the integer value of the parameter in the URL that matches
     * the provided name. If the URL contains multiple parameters, the first
     * encountered that can be casted to an int will be incremented. If no
     * parameter exists with the provided name AND cannot be casted to an int, the
     * parameter will be added with a value of 2.
     *
     * @return This object for chaining.
     */
    fun incrementParameter(name: String): URL {
        for (p in parameters) {
            if (p.getName() == name) {
                try {
                    var i = p.getValue().toInt()
                    setParameter(name, ++i)
                    return this
                } catch (e: NumberFormatException) {
                    // ignored, keep trying or return default
                }
            }
        }
        setParameter(name, 2)
        return this
    }

    /**
     * Returns an Iterator for all parameters represented in this URL. For
     * example, given the url: http://www.google.com/?param1=value1¶m2=value2
     * - this iterator would iterate over param2 and param2.
     *
     * @return An Iterator of this URLs Parameters. Never null.
     */
    override fun iterator(): MutableIterator<Parameter> {
        return parameters.iterator()
    }

    /**
     * Removes the fragment from this URL. If no fragment exists, no change will
     * occur.
     *
     * @return This object for chaining.
     */
    fun removeFragment(): URL {
        _fragment = ""
        return this
    }

    /**
     * Removes all parameters with the given name from the URL.
     *
     * @param name The
     * name(s) of the parameter(s) to remove. Null or empty string will
     * cause no changes to occur.
     * @return This object for chaining.
     */
    fun removeParameter(vararg name: String?): URL {
        if (name == null) {
            return this
        }
        for (n in name) {
            if (n == null || n.isEmpty()) {
                return this
            }
            val it = parameters.iterator()
            while (it.hasNext()) {
                val p = it.next()
                if (p.getName() == n) {
                    it.remove()
                }
            }
        }
        return this
    }

    /**
     * Sets the fragment for this URL. Any existing fragment will be overwritten.
     * A null or empty parameter will remove the fragment completely.
     *
     * @param fragment
     * @return This object for chaining.
     */
    fun setFragment(fragment: String?): URL {
        this._fragment = fragment?.let { fragmentEncode(it) } ?: ""
        return this
    }

    /**
     *
     * @param host
     * @return This object for chaining.
     */
    fun setHost(host: String?): URL {
        this.host = host ?: ""
        return this
    }

    /**
     *
     * @param name
     * @param value
     * @return This object for chaining.
     */
    fun setParameter(name: String?, value: Int): URL {
        setParameter(name, value.toString() + "")
        return this
    }

    /**
     * Sets the value of this parameter in the URL. If no parameter exists with
     * the provided name, it will be added. Example:
     *
     * <pre>
     * `URL( "http://www.shopobot.com/" ).setParameter( "page" , 1
     * ).equals( "http://www.shopobot.com/?page=1" );`
    </pre> *
     *
     * Example:
     *
     * <pre>
     * `URL( "http://www.shopobot.com/?page=1" ).setParameter( "page" , 2
     * ).equals( "http://www.shopobot.com/?page=2" );`
    </pre> *
     *
     * If more than one parameter already exists with this name, the resulting url
     * will only contain one parameter with this name and value.
     *
     * Example:
     *
     * <pre>
     * `URL( "http://www.shopobot.com/?x=1&x=2" ).setParameter( "x" , 3
     * ).equals( "http://www.shopobot.com/?x=3" );`
    </pre> *
     *
     * @return This object for chaining.
     */
    fun setParameter(name: String?, value: String?): URL {
        removeParameter(name)
        addParameter(name, value)
        return this
    }

    /**
     *
     * @param path
     * @return This object for chaining.
     */
    fun setPath(path: String): URL {
        if (path.startsWith("/")) {
            this.path = path
        } else {
            this.path = "/$path"
        }
        return this
    }

    /**
     *
     * @param port
     * @return This object for chaining.
     * @throws IllegalArgumentException
     */
    @Throws(IllegalArgumentException::class)
    fun setPort(port: Int): URL {
        require(!(port < 1 || port > 65534)) { "A valid port value is between 0 and 65535." }
        this.port = port
        return this
    }

    /**
     *
     * @param protocol
     * The protocol name, in any case, without :// Examples: http, HTTPS,
     * ftp
     * @return This object for chaining.
     * @throws IllegalArgumentException
     * If not such protocol is supported, IllegalArgumentException is
     * thrown.
     */
    @Throws(IllegalArgumentException::class)
    fun setProtocol(protocol: String): URL {
        Protocol.valueOf(protocol.lowercase(Locale.getDefault()))
        return this
    }

    /**
     * Returns the full external form of the url.
     */
    override fun toString(): String {
        // authority
        var u = authority
        // path
        u += toStringFull()
        return u
    }

    /**
     * Returns the full path, query and fragment to this item without including
     * the domain. For example, http://www.shopobot.com/search?q=1 is returned as
     * /search?q=1
     */
    fun toStringFull(): String {
        return path + queryString + fragmentString
    }// skip empty parameters

    /**
     * Helper method that just returns the query string of this URL, including a
     * leading "?". If there are no query parameters, an empty string is returned.
     */
    val queryString: String
        get() {
            var u = ""
            if (parameters.size > 0) {
                var first = true
                u += "?"
                for (param in this) {
                    if (param.toString().isEmpty()) {
                        // skip empty parameters
                        continue
                    }
                    if (first) {
                        u += param
                        first = false
                    } else {
                        u += "&$param"
                    }
                }
            }
            return u
        }

    /**
     * Helper method that just returns the fragment string of this URL, including
     * a leading "#". If there is no fragment, an empty string is returned.
     */
    val fragmentString: String
        get() {
            var u = ""
            if (!_fragment!!.isEmpty()) {
                u += "#$_fragment"
            }
            return u
        }

    fun toStringRelative(relativeTo: URL): String? {
        var target = path
        var source = relativeTo.path
        var relative: String? = ""

        // first we need to verify that all items preceding the path are identical
        if (protocol != relativeTo.protocol) {
            return toString()
        } else if (host != relativeTo.host) {
            return toString()
        } else if (getUsername() != relativeTo.getUsername()) {
            return toString()
        } else if (getPassword() != relativeTo.getPassword()) {
            return toString()
        } else if (port != relativeTo.port) {
            return toString()
        }

        // if path starts with a / remove it
        if (source!!.startsWith("/")) source = source.substring(1)
        if (target!!.startsWith("/")) target = target.substring(1)
        val sourceElements: Array<String?> = if (source.isEmpty()) arrayOfNulls(0) else source.split("/".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        val targetElements: Array<String?> = if (target.isEmpty()) arrayOfNulls(0) else target.split("/".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()

        // set int common to the number of common elements in the path
        var common = 0
        common = 0
        while (common < sourceElements.size && common < targetElements.size) {
            if (sourceElements[common] != targetElements[common]) {
                // break if we've found a non-equal path element
                break
            }
            common++
        }

        // how many times do we have to go to a previous directory to get from
        // source to target?
        val goUp = sourceElements.size - common
        if (goUp > 0) {
            for (i in 0 until goUp) {
                relative += "../"
            }
        }

        // now, ignore the common elements and tack on the new non-common path
        for (i in common until targetElements.size) {
            relative += if (relative!!.endsWith("../") || relative.isEmpty()) targetElements[i] else "/" + targetElements[i]
        }

        // since we remove the trailing slash, add it back in if requested, unless
        // the relative path is empty (because the target and source are equal
        if (target.endsWith("/") && !relative!!.isEmpty()) {
            relative += "/"
        }

        // add in any target query string or fragments
        if (queryString.isEmpty() && !relativeTo.queryString.isEmpty()) {
            // this is the most consistent way of removing all parameters
            relative = path
        } else if (queryString != relativeTo.queryString) {
            relative += queryString + fragmentString
        } else if (fragmentString != relativeTo.fragmentString) {
            relative += fragmentString
        }
        return relative
    }

    /**
     * Helper method that URL decodes a value.
     */
    private fun urlDecode(value: String): String {
        return try {
            URLDecoder.decode(value, ENCODING)
        } catch (e: UnsupportedEncodingException) {
            throw IllegalArgumentException(ENCODING + ENCODING_ERROR, e)
        }
    }

    /**
     * Helper method that URL encodes a value.
     */
    private fun urlEncode(value: String): String {
        return try {
            URLEncoder.encode(value, ENCODING)
        } catch (e: UnsupportedEncodingException) {
            throw IllegalArgumentException(ENCODING + ENCODING_ERROR, e)
        }
    }

    /**
     * Helper method used to parse a query string and add the parameters.
     *
     * @param queryString
     * Either a whole URL ("http://www.shopobot.com/?query=test") or just
     * a query string ("?query=test"). If null or "" is pased, this
     * method silently returns without changing anything.
     */
    private fun parseQueryString(queryString: String): URL {
        // parse query parameters
        var queryString: String? = queryString
        if (queryString == null || queryString.isEmpty()) {
            return this
        }
        var pos = queryString.indexOf('?')
        if (pos > -1) {
            val query = queryString.substring(pos + 1)
            queryString = queryString.substring(0, pos)
            for (param in query.split("&".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()) {
                pos = param.indexOf("=")
                if (pos > -1) {
                    // regular parameter
                    addParameterNoEncode(param.substring(0, pos), param.substring(pos + 1))
                } else {
                    // empty parameter
                    addParameterNoEncode(param, "")
                }
            }
        }
        return this
    }

    /**
     * Returns an instance of java.net.URL that represents this URL. If the URL is
     * malformed, null is returned.
     */
    fun toJavaURL(): java.net.URL? {
        return try {
            java.net.URL(toString())
        } catch (e: MalformedURLException) {
            null
        }
    }

    /**
     * Returns an instance of java.net.URL that represents this URL. If the URL is
     * malformed, null is returned.
     */
    fun toJavaURI(): URI? {
        return try {
            URI(toString())
        } catch (e: URISyntaxException) {
            null
        }
    }// protocol

    // user info

    // host, port
    /**
     * Returns "http://www.google.com:80" for the URL
     * "http://www.google.com:80/search&q=test"
     *
     * @return
     */
    val authority: String
        get() {
            // protocol
            var u = protocol.name + "://"

            // user info
            if (!_username.isEmpty()) {
                u += if (!_password.isEmpty()) {
                    "$_username:$_password@"
                } else {
                    "$_username@"
                }
            }

            // host, port
            u += if (port != protocol.defaultPort) {
                "$host:$port"
            } else {
                host
            }
            return u
        }

    /**
     * Tests if this URL object matches the authority of the provided String (or
     * URL). Example: new
     * URL("www.subdomain.example.com").matchesAuthority("com"); //true new
     * URL("www.subdomain.example.com").matchesAuthority("example.com"); //true
     * new
     * URL("www.subdomain.example.com").matchesAuthority("subdomain.example.com");
     * //true new URL("www.subdomain.example.com").matchesAuthority(
     * "www.subdomain.example.com"); //true
     *
     * @param authority
     * A string reresenting the authority to check against this url.
     * @return true if the authority matches, false in all other cases (including
     * if a null or empty parameter is passed).
     */
    fun matchesAuthority(authority: String?): Boolean {
        var authority = authority
        if (authority == null || authority.isEmpty()) {
            return false
        }
        // true if a direct match (ex: www.amazon.com and www.amazon.com)
        if (host == authority) {
            return true
        }
        // if they passed a domain starting with "." remove it
        if (authority.startsWith(".")) {
            authority = authority.substring(1)
        }
        // true if a subdomain match (ex: www.amazon.com and amazon.com)
        return host.endsWith(".$authority")
    }

    /**
     * Tests if this URL object matches the authority of the provided String (or
     * URL). Example: new
     * URL("www.subdomain.example.com").matchesAuthority("com"); //true new
     * URL("www.subdomain.example.com").matchesAuthority("example.com"); //true
     * new
     * URL("www.subdomain.example.com").matchesAuthority("subdomain.example.com");
     * //true new URL("www.subdomain.example.com").matchesAuthority(
     * "www.subdomain.example.com"); //true
     *
     * @return true if the authority matches, false in all other cases (including
     * if a null or empty parameter is passed).
     */
    fun matchesAuthority(url: URL): Boolean {
        return matchesAuthority(url.host)
    }

    /**
     * Checks if any of the authorities provided match this URL.  If one matches, true is returned.
     */
    fun matchesAuthority(vararg authority: String?): Boolean {
        for (s in authority) {
            if (matchesAuthority(s)) {
                return true
            }
        }
        return false
    }

    /**
     * Returns number of authority elements. new
     * URL("shopobot.com").getAuthoritySize(2); new
     * URL("en.shopobot.com").getAuthoritySize(3); new
     * URL("www.en.shopobot.com").getAuthoritySize(4);
     */
    fun getAuthoritySize(): Int {
        return host.split("\\.".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray().size
    }

    /**
     * Returns the authority to position i. For example, given the domain:
     * www.en.shopobot.com, these statements are all true:
     * getAuthority(0).equals("com"); getAuthority(1).equals("shopobot.com");
     * getAuthority(2).equals("en.shopobot.com");
     * getAuthority(3).equals("www.en.shopobot.com");
     * getAuthority(4).equals("www.en.shopobot.com");
     */
    fun getAuthority(i: Int): String {
        if (i <= 0) return ""
        val s = host.split("\\.".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        var r = ""
        var k = 1
        for (j in s.indices.reversed()) {
            r = "." + s[j] + r
            if (k++ >= i) {
                break
            }
        }
        if (r.startsWith(".")) {
            r = r.substring(1)
        }
        return r
    }

    companion object {
        private const val ENCODING = "UTF-8"
        private const val ENCODING_ERROR = " is not a supported encoding option"

        /**
         * Returns a URL for the provided String, or null if the URL provided is
         * invalid.
         *
         * @param url
         * The external form of a URL.
         * @return A URL object for the provided String, or null if the URL provided
         * is invalid.
         */
        operator fun get(url: String?): URL? {
            return try {
                URL(url)
            } catch (e: Exception) {
                null
            }
        }

        /**
         * Returns a URL for the provided String, or the provided default URL if the
         * URL provided is invalid.
         *
         * @param url
         * The external form of a URL.
         * @param defaul
         * The URL that should be returned in cases where the primary url is
         * invalid.
         * @return A URL object for the provided String, or null if the URL provided
         * is invalid.
         */
        operator fun get(url: String?, defaul: URL): URL {
            return try {
                URL(url)
            } catch (e: Exception) {
                defaul
            }
        }

        /**
         * Tests if the provided URL is valid.
         *
         * @param url
         * The URL to test.
         * @return true if the URL can be instantiated, false otherwise.
         */
        fun isValid(url: String?): Boolean {
            try {
                URL(url)
            } catch (e: Exception) {
                return false
            }
            return true
        }
    }
}