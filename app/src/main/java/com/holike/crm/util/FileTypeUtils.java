package com.holike.crm.util;

import android.text.TextUtils;

import java.io.File;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class FileTypeUtils {
    private Map<String, String> mimeTypeMap;

    private volatile static FileTypeUtils instance;

    private FileTypeUtils() {
        mimeTypeMap = new HashMap<>();
        mimeTypeMap.put("3dm", "x-world/x-3dmf");
        mimeTypeMap.put("3dmf", "x-world/x-3dmf");
        mimeTypeMap.put("a", "application/octet-stream");
        mimeTypeMap.put("aab", "application/x-authorware-bin");
        mimeTypeMap.put("aam", "application/x-authorware-map");
        mimeTypeMap.put("aas", "application/x-authorware-seg");
        mimeTypeMap.put("abc", "text/vndabc");
        mimeTypeMap.put("acgi", "text/html");
        mimeTypeMap.put("afl", "video/animaflex");
        mimeTypeMap.put("ai", "application/postscript");
        mimeTypeMap.put("aif", "audio/aiff");
        mimeTypeMap.put("aifc", "audio/aiff");
        mimeTypeMap.put("aiff", "audio/aiff");
        mimeTypeMap.put("aim", "application/x-aim");
        mimeTypeMap.put("aip", "text/x-audiosoft-intra");
        mimeTypeMap.put("ani", "application/x-navi-animation");
        mimeTypeMap.put("aos", "application/x-nokia-9000-communicator-add-on-software");
        mimeTypeMap.put("aps", "application/mime");
        mimeTypeMap.put("arc", "application/octet-stream");
        mimeTypeMap.put("arj", "application/arj");
        mimeTypeMap.put("art", "image/x-jg");
        mimeTypeMap.put("asf", "video/x-ms-asf");
        mimeTypeMap.put("asm", "text/x-asm");
        mimeTypeMap.put("asp", "text/asp");
        mimeTypeMap.put("asx", "application/x-mplayer2");
        mimeTypeMap.put("au", "audio/basic");
        mimeTypeMap.put("avi", "video/avi");
        mimeTypeMap.put("avs", "video/avs-video");
        mimeTypeMap.put("bcpio", "application/x-bcpio");
        mimeTypeMap.put("bin", "application/x-macbinary");
        mimeTypeMap.put("bm", "image/bmp");
        mimeTypeMap.put("bmp", "image/bmp");
        mimeTypeMap.put("boo", "application/book");
        mimeTypeMap.put("book", "application/book");
        mimeTypeMap.put("boz", "application/x-bzip2");
        mimeTypeMap.put("bsh", "application/x-bsh");
        mimeTypeMap.put("bz", "application/x-bzip");
        mimeTypeMap.put("bz2", "application/x-bzip2");
        mimeTypeMap.put("c", "text/plain");
        mimeTypeMap.put("c++", "text/plain");
        mimeTypeMap.put("cat", "application/vndms-pkiseccat");
        mimeTypeMap.put("cc", "text/plain");
        mimeTypeMap.put("ccad", "application/clariscad");
        mimeTypeMap.put("cco", "application/x-cocoa");
        mimeTypeMap.put("cdf", "application/cdf");
        mimeTypeMap.put("cer", "application/pkix-cert");
        mimeTypeMap.put("cha", "application/x-chat");
        mimeTypeMap.put("chat", "application/x-chat");
        mimeTypeMap.put("class", "application/java");
        mimeTypeMap.put("com", "text/plain");
        mimeTypeMap.put("conf", "text/plain");
        mimeTypeMap.put("cpio", "application/x-cpio");
        mimeTypeMap.put("cpp", "text/x-c");
        mimeTypeMap.put("cpt", "application/mac-compactpro");
        mimeTypeMap.put("crl", "application/pkcs-crl");
        mimeTypeMap.put("crt", "application/pkix-cert");
        mimeTypeMap.put("csh", "application/x-csh");
        mimeTypeMap.put("css", "text/css");
        mimeTypeMap.put("cxx", "text/plain");
        mimeTypeMap.put("dcr", "application/x-director");
        mimeTypeMap.put("deepv", "application/x-deepv");
        mimeTypeMap.put("def", "text/plain");
        mimeTypeMap.put("der", "application/x-x509-ca-cert");
        mimeTypeMap.put("dif", "video/x-dv");
        mimeTypeMap.put("dir", "application/x-director");
        mimeTypeMap.put("dl", "video/dl");
        mimeTypeMap.put("doc", "application/msword");
        mimeTypeMap.put("dot", "application/msword");
        mimeTypeMap.put("dp", "application/commonground");
        mimeTypeMap.put("drw", "application/drafting");
        mimeTypeMap.put("dump", "application/octet-stream");
        mimeTypeMap.put("dv", "video/x-dv");
        mimeTypeMap.put("dvi", "application/x-dvi");
        mimeTypeMap.put("dwf", "model/vnddwf");
        mimeTypeMap.put("dwg", "application/acad");
        mimeTypeMap.put("dxf", "application/dxf");
        mimeTypeMap.put("dxr", "application/x-director");
        mimeTypeMap.put("el", "text/x-scriptelisp");
        mimeTypeMap.put("elc", "application/x-elc");
        mimeTypeMap.put("env", "application/x-envoy");
        mimeTypeMap.put("eps", "application/postscript");
        mimeTypeMap.put("es", "application/x-esrehber");
        mimeTypeMap.put("etx", "text/x-setext");
        mimeTypeMap.put("evy", "application/envoy");
        mimeTypeMap.put("exe", "application/octet-stream");
        mimeTypeMap.put("f", "text/plain");
        mimeTypeMap.put("f77", "text/x-fortran");
        mimeTypeMap.put("f90", "text/plain");
        mimeTypeMap.put("fdf", "application/vndfdf");
        mimeTypeMap.put("fif", "application/fractals");
        mimeTypeMap.put("fli", "video/fli");
        mimeTypeMap.put("flo", "image/florian");
        mimeTypeMap.put("flv", "video/x-flv");
        mimeTypeMap.put("flx", "text/vndfmiflexstor");
        mimeTypeMap.put("fmf", "video/x-atomic3d-feature");
        mimeTypeMap.put("for", "text/plain");
        mimeTypeMap.put("fpx", "image/vndfpx");
        mimeTypeMap.put("frl", "application/freeloader");
        mimeTypeMap.put("funk", "audio/make");
        mimeTypeMap.put("g", "text/plain");
        mimeTypeMap.put("g3", "image/g3fax");
        mimeTypeMap.put("gif", "image/gif");
        mimeTypeMap.put("gl", "video/gl");
        mimeTypeMap.put("gsd", "audio/x-gsm");
        mimeTypeMap.put("gsm", "audio/x-gsm");
        mimeTypeMap.put("gsp", "application/x-gsp");
        mimeTypeMap.put("gss", "application/x-gss");
        mimeTypeMap.put("gtar", "application/x-gtar");
        mimeTypeMap.put("gz", "application/x-compressed");
        mimeTypeMap.put("gzip", "application/x-gzip");
        mimeTypeMap.put("h", "text/plain");
        mimeTypeMap.put("hdf", "application/x-hdf");
        mimeTypeMap.put("help", "application/x-helpfile");
        mimeTypeMap.put("hgl", "application/vndhp-hpgl");
        mimeTypeMap.put("hh", "text/plain");
        mimeTypeMap.put("hlb", "text/x-script");
        mimeTypeMap.put("hlp", "application/hlp");
        mimeTypeMap.put("hpg", "application/vndhp-hpgl");
        mimeTypeMap.put("hpgl", "application/vndhp-hpgl");
        mimeTypeMap.put("hqx", "application/binhex");
        mimeTypeMap.put("hta", "application/hta");
        mimeTypeMap.put("htc", "text/x-component");
        mimeTypeMap.put("htm", "text/html");
        mimeTypeMap.put("html", "text/html");
        mimeTypeMap.put("htmls", "text/html");
        mimeTypeMap.put("htt", "text/webviewhtml");
        mimeTypeMap.put("htx", "text/html");
        mimeTypeMap.put("ice", "x-conference/x-cooltalk");
        mimeTypeMap.put("ico", "image/x-icon");
        mimeTypeMap.put("idc", "text/plain");
        mimeTypeMap.put("ief", "image/ief");
        mimeTypeMap.put("iefs", "image/ief");
        mimeTypeMap.put("iges", "application/iges");
        mimeTypeMap.put("igs", "application/iges");
        mimeTypeMap.put("ima", "application/x-ima");
        mimeTypeMap.put("imap", "application/x-httpd-imap");
        mimeTypeMap.put("inf", "application/inf");
        mimeTypeMap.put("ins", "application/x-internett-signup");
        mimeTypeMap.put("ip", "application/x-ip2");
        mimeTypeMap.put("isu", "video/x-isvideo");
        mimeTypeMap.put("it", "audio/it");
        mimeTypeMap.put("iv", "application/x-inventor");
        mimeTypeMap.put("ivr", "i-world/i-vrml");
        mimeTypeMap.put("ivy", "application/x-livescreen");
        mimeTypeMap.put("jam", "audio/x-jam");
        mimeTypeMap.put("jav", "text/plain");
        mimeTypeMap.put("java", "text/plain");
        mimeTypeMap.put("jcm", "application/x-java-commerce");
        mimeTypeMap.put("jgif", "image/jpeg");
        mimeTypeMap.put("jfif-tbnl", "image/jpeg");
        mimeTypeMap.put("jpe", "image/jpeg");
        mimeTypeMap.put("jpeg", "image/jpeg");
        mimeTypeMap.put("jpg", "image/jpeg");
        mimeTypeMap.put("jps", "image/x-jps");
        mimeTypeMap.put("js", "application/x-javascript");
        mimeTypeMap.put("jut", "image/jutvision");
        mimeTypeMap.put("kar", "audio/midi");
        mimeTypeMap.put("ksh", "application/x-ksh");
        mimeTypeMap.put("la", "audio/nspaudio");
        mimeTypeMap.put("lam", "audio/x-liveaudio");
        mimeTypeMap.put("latex", "application/x-latex");
        mimeTypeMap.put("lha", "application/lha");
        mimeTypeMap.put("lhx", "application/octet-stream");
        mimeTypeMap.put("list", "text/plain");
        mimeTypeMap.put("lma", "audio/nspaudio");
        mimeTypeMap.put("log", "text/plain");
        mimeTypeMap.put("lsp", "application/x-lisp");
        mimeTypeMap.put("lst", "text/plain");
        mimeTypeMap.put("lsx", "text/x-la-asf");
        mimeTypeMap.put("ltx", "application/x-latex");
        mimeTypeMap.put("lzh", "application/octet-stream");
        mimeTypeMap.put("lzx", "application/lzx");
        mimeTypeMap.put("m", "text/plain");
        mimeTypeMap.put("m1v", "video/mpeg");
        mimeTypeMap.put("m2a", "audio/mpeg");
        mimeTypeMap.put("m2v", "video/mpeg");
        mimeTypeMap.put("m3u", "audio/x-mpequrl");
        mimeTypeMap.put("man", "application/x-troff-man");
        mimeTypeMap.put("map", "application/x-navimap");
        mimeTypeMap.put("mar", "text/plain");
        mimeTypeMap.put("mbd", "application/mbedlet");
        mimeTypeMap.put("mc$", "application/x-magic-cap-package-10");
        mimeTypeMap.put("mcd", "application/mcad");
        mimeTypeMap.put("mcf", "image/vasa");
        mimeTypeMap.put("mcp", "application/netmc");
        mimeTypeMap.put("me", "application/x-troff-me");
        mimeTypeMap.put("mht", "message/rfc822");
        mimeTypeMap.put("mhtml", "message/rfc822");
        mimeTypeMap.put("midi", "audio/midi");
        mimeTypeMap.put("mif", "application/x-frame");
        mimeTypeMap.put("mime", "message/rfc822");
        mimeTypeMap.put("mjf", "audio/x-vndaudioexplosionmjuicemediafile");
        mimeTypeMap.put("mjpg", "video/x-motion-jpeg");
        mimeTypeMap.put("mkv", "video/x-matroska");
        mimeTypeMap.put("mka", "audio/x-matroska");
        mimeTypeMap.put("mm", "application/x-meme");
        mimeTypeMap.put("mme", "application/base64");
        mimeTypeMap.put("mod", "audio/mod");
        mimeTypeMap.put("mov", "video/quicktime");
        mimeTypeMap.put("movie", "video/x-sgi-movie");
        mimeTypeMap.put("mp2", "audio/mpeg");
        mimeTypeMap.put("mp3", "audio/mp3");
        mimeTypeMap.put("mp4", "video/mp4");
        mimeTypeMap.put("mpa", "audio/mpeg");
        mimeTypeMap.put("mpc", "application/x-project");
        mimeTypeMap.put("mpe", "video/mpeg");
        mimeTypeMap.put("mpeg", "video/mpeg");
        mimeTypeMap.put("mpg", "audio/mpeg");
        mimeTypeMap.put("mpga", "audio/mpeg");
        mimeTypeMap.put("mpp", "application/vndms-project");
        mimeTypeMap.put("mpt", "application/x-project");
        mimeTypeMap.put("mpv", "application/x-project");
        mimeTypeMap.put("mpx", "application/x-project");
        mimeTypeMap.put("mrc", "application/marc");
        mimeTypeMap.put("ms", "application/x-troff-ms");
        mimeTypeMap.put("mv", "video/x-sgi-movie");
        mimeTypeMap.put("my", "audio/make");
        mimeTypeMap.put("mzz", "application/x-vndaudioexplosionmzz");
        mimeTypeMap.put("nap", "image/naplps");
        mimeTypeMap.put("naplps", "image/naplps");
        mimeTypeMap.put("nc", "application/x-netcdf");
        mimeTypeMap.put("ncm", "application/vndnokiaconfiguration-message");
        mimeTypeMap.put("nif", "image/x-niff");
        mimeTypeMap.put("niff", "image/x-niff");
        mimeTypeMap.put("nix", "application/x-mix-transfer");
        mimeTypeMap.put("nsc", "application/x-conference");
        mimeTypeMap.put("nvd", "application/x-navidoc");
        mimeTypeMap.put("o", "application/octet-stream");
        mimeTypeMap.put("oda", "application/oda");
        mimeTypeMap.put("omc", "application/x-omc");
        mimeTypeMap.put("omcd", "application/x-omcdatamaker");
        mimeTypeMap.put("omcr", "application/x-omcregerator");
        mimeTypeMap.put("p", "text/x-pascal");
        mimeTypeMap.put("p10", "application/pkcs10");
        mimeTypeMap.put("p12", "application/pkcs-12");
        mimeTypeMap.put("p7a", "application/x-pkcs7-signature");
        mimeTypeMap.put("p7c", "application/pkcs7-mime");
        mimeTypeMap.put("p7m", "application/pkcs7-mime");
        mimeTypeMap.put("p7r", "application/x-pkcs7-certreqresp");
        mimeTypeMap.put("p7s", "application/pkcs7-signature");
        mimeTypeMap.put("part", "application/pro_eng");
        mimeTypeMap.put("pas", "text/pascal");
        mimeTypeMap.put("pbm", "image/x-portable-bitmap");
        mimeTypeMap.put("pcl", "application/vndhp-pcl");
        mimeTypeMap.put("pct", "image/x-pict");
        mimeTypeMap.put("pcx", "image/x-pcx");
        mimeTypeMap.put("pdb", "chemical/x-pdb");
        mimeTypeMap.put("pdf", "application/pdf");
        mimeTypeMap.put("pfunk", "audio/make");
        mimeTypeMap.put("pgm", "image/x-portable-graymap");
        mimeTypeMap.put("pic", "image/pict");
        mimeTypeMap.put("pict", "image/pict");
        mimeTypeMap.put("pkg", "application/x-newton-compatible-pkg");
        mimeTypeMap.put("pko", "application/vndms-pkipko");
        mimeTypeMap.put("pl", "text/plain");
        mimeTypeMap.put("plx", "application/x-pixclscript");
        mimeTypeMap.put("pm", "image/x-xpixmap");
        mimeTypeMap.put("pm4", "application/x-pagemaker");
        mimeTypeMap.put("pm5", "application/x-pagemaker");
        mimeTypeMap.put("png", "image/png");
        mimeTypeMap.put("pnm", "application/x-portable-anymap");
        mimeTypeMap.put("pot", "application/mspowerpoint");
        mimeTypeMap.put("pov", "model/x-pov");
        mimeTypeMap.put("ppa", "application/vndms-powerpoint");
        mimeTypeMap.put("ppm", "image/x-portable-pixmap");
        mimeTypeMap.put("pps", "application/mspowerpoint");
        mimeTypeMap.put("ppt", "application/mspowerpoint");
        mimeTypeMap.put("ppz", "application/mspowerpoint");
        mimeTypeMap.put("pre", "application/x-freelance");
        mimeTypeMap.put("prt", "application/pro_eng");
        mimeTypeMap.put("ps", "application/postscript");
        mimeTypeMap.put("psd", "application/octet-stream");
        mimeTypeMap.put("pvu", "paleovu/x-pv");
        mimeTypeMap.put("pwz", "application/vndms-powerpoint");
        mimeTypeMap.put("py", "text/x-scriptphyton");
        mimeTypeMap.put("pyc", "applicaiton/x-bytecodepython");
        mimeTypeMap.put("qcp", "audio/vndqcelp");
        mimeTypeMap.put("qd3", "x-world/x-3dmf");
        mimeTypeMap.put("qd3d", "x-world/x-3dmf");
        mimeTypeMap.put("qif", "image/x-quicktime");
        mimeTypeMap.put("qt", "video/quicktime");
        mimeTypeMap.put("qtc", "video/x-qtc");
        mimeTypeMap.put("qti", "image/x-quicktime");
        mimeTypeMap.put("qtif", "image/x-quicktime");
        mimeTypeMap.put("ra", "audio/x-pn-realaudio");
        mimeTypeMap.put("ram", "audio/x-pn-realaudio");
        mimeTypeMap.put("ras", "application/x-cmu-raster");
        mimeTypeMap.put("rast", "image/cmu-raster");
        mimeTypeMap.put("rexx", "text/x-scriptrexx");
        mimeTypeMap.put("rf", "image/vndrn-realflash");
        mimeTypeMap.put("rgb", "image/x-rgb");
        mimeTypeMap.put("rm", "application/vndrn-realmedia");
        mimeTypeMap.put("rmi", "audio/mid");
        mimeTypeMap.put("rmm", "audio/x-pn-realaudio");
        mimeTypeMap.put("rmp", "audio/x-pn-realaudio");
        mimeTypeMap.put("rng", "application/ringing-tones");
        mimeTypeMap.put("rnx", "application/vndrn-realplayer");
        mimeTypeMap.put("roff", "application/x-troff");
        mimeTypeMap.put("rp", "image/vndrn-realpix");
        mimeTypeMap.put("rpm", "audio/x-pn-realaudio-plugin");
        mimeTypeMap.put("rt", "text/richtext");
        mimeTypeMap.put("rtf", "application/rtf");
        mimeTypeMap.put("rtx", "application/rtf");
        mimeTypeMap.put("rv", "video/vndrn-realvideo");
        mimeTypeMap.put("s", "text/x-asm");
        mimeTypeMap.put("s3m", "audio/s3m");
        mimeTypeMap.put("saveme", "application/octet-stream");
        mimeTypeMap.put("sbk", "application/x-tbook");
        mimeTypeMap.put("scm", "application/x-lotusscreencam");
        mimeTypeMap.put("sdml", "text/plain");
        mimeTypeMap.put("sdp", "application/sdp");
        mimeTypeMap.put("sdr", "application/sounder");
        mimeTypeMap.put("sea", "application/sea");
        mimeTypeMap.put("set", "application/set");
        mimeTypeMap.put("sgm", "text/sgml");
        mimeTypeMap.put("sgml", "text/sgml");
        mimeTypeMap.put("sh", "application/x-bsh");
        mimeTypeMap.put("shar", "application/x-bsh");
        mimeTypeMap.put("shtml", "text/html");
        mimeTypeMap.put("sid", "audio/x-psid");
        mimeTypeMap.put("sit", "application/x-sit");
        mimeTypeMap.put("skd", "application/x-koan");
        mimeTypeMap.put("skm", "application/x-koan");
        mimeTypeMap.put("skp", "application/x-koan");
        mimeTypeMap.put("skt", "application/x-koan");
        mimeTypeMap.put("sl", "application/x-seelogo");
        mimeTypeMap.put("smi", "application/smil");
        mimeTypeMap.put("smil", "application/smil");
        mimeTypeMap.put("snd", "audio/basic");
        mimeTypeMap.put("sol", "application/solids");
        mimeTypeMap.put("spc", "application/x-pkcs7-certificates");
        mimeTypeMap.put("spl", "application/futuresplash");
        mimeTypeMap.put("spr", "application/x-sprite");
        mimeTypeMap.put("sprite", "application/x-sprite");
        mimeTypeMap.put("src", "application/x-wais-source");
        mimeTypeMap.put("ssi", "text/x-server-parsed-html");
        mimeTypeMap.put("ssm", "application/streamingmedia");
        mimeTypeMap.put("sst", "application/vndms-pkicertstore");
        mimeTypeMap.put("step", "application/step");
        mimeTypeMap.put("stl", "application/sla");
        mimeTypeMap.put("stp", "application/step");
        mimeTypeMap.put("sv4cpio", "application/x-sv4cpio");
        mimeTypeMap.put("sv4crc", "application/x-sv4crc");
        mimeTypeMap.put("svf", "image/vnddwg");
        mimeTypeMap.put("swf", "application/x-shockwave-flash");
        mimeTypeMap.put("t", "application/x-troff");
        mimeTypeMap.put("talk", "text/x-speech");
        mimeTypeMap.put("tar", "application/x-tar");
        mimeTypeMap.put("tbk", "application/toolbook");
        mimeTypeMap.put("tcl", "application/x-tcl");
        mimeTypeMap.put("tcsh", "text/x-scripttcsh");
        mimeTypeMap.put("tex", "application/x-tex");
        mimeTypeMap.put("texi", "application/x-texinfo");
        mimeTypeMap.put("texinfo", "application/x-texinfo");
        mimeTypeMap.put("text", "text/plain");
        mimeTypeMap.put("tgz", "application/gnutar");
        mimeTypeMap.put("tif", "image/tiff");
        mimeTypeMap.put("tiff", "image/tiff");
        mimeTypeMap.put("tr", "application/x-troff");
        mimeTypeMap.put("tsi", "audio/tsp-audio");
        mimeTypeMap.put("tsp", "application/dsptype");
        mimeTypeMap.put("tsv", "text/tab-separated-values");
        mimeTypeMap.put("turbot", "image/florian");
        mimeTypeMap.put("txt", "text/plain");
        mimeTypeMap.put("uil", "text/x-uil");
        mimeTypeMap.put("uni", "text/uri-list");
        mimeTypeMap.put("unis", "text/uri-list");
        mimeTypeMap.put("unv", "application/i-deas");
        mimeTypeMap.put("uri", "text/uri-list");
        mimeTypeMap.put("uris", "text/uri-list");
        mimeTypeMap.put("ustar", "application/x-ustar");
        mimeTypeMap.put("uu", "application/octet-stream");
        mimeTypeMap.put("uue", "text/x-uuencode");
        mimeTypeMap.put("vcd", "application/x-cdlink");
        mimeTypeMap.put("vcs", "text/x-vcalendar");
        mimeTypeMap.put("vda", "application/vda");
        mimeTypeMap.put("vdo", "video/vdo");
        mimeTypeMap.put("vew", "application/groupwise");
        mimeTypeMap.put("viv", "video/vivo");
        mimeTypeMap.put("vivo", "video/vivo");
        mimeTypeMap.put("vmd", "application/vocaltec-media-desc");
        mimeTypeMap.put("vmf", "application/vocaltec-media-file");
        mimeTypeMap.put("voc", "audio/voc");
        mimeTypeMap.put("vos", "video/vosaic");
        mimeTypeMap.put("vox", "audio/voxware");
        mimeTypeMap.put("vqe", "audio/x-twinvq-plugin");
        mimeTypeMap.put("vqf", "audio/x-twinvq");
        mimeTypeMap.put("vql", "audio/x-twinvq-plugin");
        mimeTypeMap.put("vrml", "application/x-vrml");
        mimeTypeMap.put("vrt", "x-world/x-vrt");
        mimeTypeMap.put("vsd", "application/x-visio");
        mimeTypeMap.put("vst", "application/x-visio");
        mimeTypeMap.put("vsw", "application/x-visio");
        mimeTypeMap.put("w60", "application/wordperfect60");
        mimeTypeMap.put("w61", "application/wordperfect61");
        mimeTypeMap.put("w6w", "application/msword");
        mimeTypeMap.put("wav", "audio/wav");
        mimeTypeMap.put("wb1", "application/x-qpro");
        mimeTypeMap.put("wbmp", "image/vndwapwbmp");
        mimeTypeMap.put("web", "application/vndxara");
        mimeTypeMap.put("wiz", "application/msword");
        mimeTypeMap.put("wk1", "application/x-123");
        mimeTypeMap.put("wmf", "windows/metafile");
        mimeTypeMap.put("wml", "text/vndwapwml");
        mimeTypeMap.put("wmlc", "application/vndwapwmlc");
        mimeTypeMap.put("wmls", "text/vndwapwmlscript");
        mimeTypeMap.put("wmlsc", "application/vndwapwmlscriptc");
        mimeTypeMap.put(".wmv", "video/x-ms-wmv");
        mimeTypeMap.put("word", "application/msword");
        mimeTypeMap.put("wp", "application/wordperfect");
        mimeTypeMap.put("wp5", "application/wordperfect");
        mimeTypeMap.put("wp6", "application/wordperfect");
        mimeTypeMap.put("wpd", "application/wordperfect");
        mimeTypeMap.put("wq1", "application/x-lotus");
        mimeTypeMap.put("wri", "application/mswrite");
        mimeTypeMap.put("wrl", "application/x-world");
        mimeTypeMap.put("wrz", "model/vrml");
        mimeTypeMap.put("wsc", "text/scriplet");
        mimeTypeMap.put("wsrc", "application/x-wais-source");
        mimeTypeMap.put("webm", "video/webm");
        mimeTypeMap.put("wtk", "application/x-wintalk");
        mimeTypeMap.put("xbm", "image/x-xbitmap");
        mimeTypeMap.put("xdr", "video/x-amt-demorun");
        mimeTypeMap.put("xgz", "xgl/drawing");
        mimeTypeMap.put("xif", "image/vndxiff");
        mimeTypeMap.put("xl", "application/excel");
        mimeTypeMap.put("xla", "application/excel");
        mimeTypeMap.put("xlb", "application/excel");
        mimeTypeMap.put("xlc", "application/excel");
        mimeTypeMap.put("xld", "application/excel");
        mimeTypeMap.put("xlk", "application/excel");
        mimeTypeMap.put("xll", "application/excel");
        mimeTypeMap.put("xlm", "application/excel");
        mimeTypeMap.put("xls", "application/excel");
        mimeTypeMap.put("xlt", "application/excel");
        mimeTypeMap.put("xlv", "application/excel");
        mimeTypeMap.put("xlw", "application/excel");
        mimeTypeMap.put("xm", "audio/xm");
        mimeTypeMap.put("xml", "text/xml");
        mimeTypeMap.put("xmz", "xgl/movie");
        mimeTypeMap.put("xpix", "application/x-vndls-xpix");
        mimeTypeMap.put("xpm", "image/xpm");
        mimeTypeMap.put("x-png", "image/png");
        mimeTypeMap.put("xsr", "video/x-amt-showrun");
        mimeTypeMap.put("xwd", "image/x-xwd");
        mimeTypeMap.put("xyz", "chemical/x-pdb");
        mimeTypeMap.put("z", "application/x-compress");
        mimeTypeMap.put("zip", "application/zip");
        mimeTypeMap.put("zoo", "application/octet-stream");
        mimeTypeMap.put("zsh", "text/x-scriptzsh");
    }

    public static FileTypeUtils getInstance() {
        if (instance == null) {
            synchronized (FileTypeUtils.class) {
                if (instance == null) {
                    instance = new FileTypeUtils();
                }
            }
        }
        return instance;
    }

    public static String getMimeType(String filePath) {
        return getExtension(new File(filePath));
    }

    public static String getMimeType(URL url) {
        return getMimeType(new File(url.getFile()));
    }

    private static String getExtension(File file) {
        String name = file.getName();
        return getExtension(name);
    }

    private static String getExtension(String name) {
        int i = name.lastIndexOf(".");
        return i >= 0 ? name.substring(i + 1) : null;
    }

    public static void addMimeType(String extension, String mimeType) {
        getInstance().mimeTypeMap.put(extension, mimeType);
    }

    public static String getMimeType(File file) {
        String name = file.getName();
        if (!TextUtils.isEmpty(name)) {
            int i = file.getName().lastIndexOf(".");
            if (i >= 0) {
                String extension = getExtension(file);
                if (TextUtils.isEmpty(extension)) {
                    return "application/octet-stream";
                } else {
                    return TextUtils.isEmpty(getInstance().mimeTypeMap.get(extension)) ? "application/octet-stream" : getInstance().mimeTypeMap.get(extension);
                }
            }
        }
        return "application/octet-stream";
    }
}
