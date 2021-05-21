package utils;

import enums.Protocols;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * @Author: TangXiaoDong
 * @Date: 14:14 2021/5/19
 * @Description:
 */
public final class ClassUtil {

    private static final Logger log = LoggerFactory.getLogger(ClassUtil.class);

    /**
     * 获取类加载器
     * @return
     */
    public static ClassLoader getClassLoader() {
        return Thread.currentThread().getContextClassLoader();
    }


    /**
     * 加载类
     * @param classname 加载的类名
     * @param isInitialized 是否初始化
     * @return
     */
    // <T> 申明泛型类或泛型方法 <?> 无限定通配符 使用泛型类和泛型方法
    public static Class<?> loadClass(String classname, boolean isInitialized)  {
        Class<?> cls;
        try {
            cls = Class.forName(classname, isInitialized, getClassLoader());
        } catch (ClassNotFoundException e) {
            log.error("load class failure: ", e);
            throw new RuntimeException(e);
        }
        return cls;
    }


    /**
     * 加载类，默认初始化类
     * @param classname
     * @return
     */
    public static Class<?> loadClass(String classname) {
        return loadClass(classname, true);
    }


    /**
     * 获取指定包下所有的类
     * @param packageName 包名
     * @return
     */
    public static Set<Class<?>> getClassSet(String packageName) {
        Set<Class<?>> classSet = new HashSet<Class<?>>();
        try {
            Enumeration<URL> urls = getClassLoader().getResources(packageName.replace(".", "/"));
            while (urls.hasMoreElements()) {
                URL url = urls.nextElement();
                if (url != null) {
                    String protocol = url.getProtocol();
                    if (Protocols.file.equals(protocol)) { // 读取的是文件
                        // 1、url.getPath().replaceAll("%20", "") 将路径上的所有%20替换成空格，但是仅仅只能解决空格问题
                        // 2、URLDecoder.decode(str,“UTF-8”)解码，但是只能解决一部分问题，因为URL不是所有的都是使用UTF-8进行编码的
                        String packagePath = URLDecoder.decode(url.getPath(), "UTF-8");
                        // 添加相关类到Set类集合中
                        addClass(classSet, packagePath, packageName);
                    } else if (Protocols.jar.equals(protocol)) { // 读取的是jar，并需要jar的某些特定功能
                        JarURLConnection jarURLConn = (JarURLConnection) url.openConnection();
                        JarFile jarFile = jarURLConn.getJarFile();
                        if (jarFile != null) {
                            Enumeration<JarEntry> jarEntries = jarFile.entries();
                            while (jarEntries.hasMoreElements()) {
                                // 得到jarEntry给定条目
                                JarEntry jarEntry = jarEntries.nextElement();
                                String jarEntryName = jarEntry.getName();
                                if (".class".endsWith(jarEntryName)) { // 类文件
                                    String classname = jarEntryName.substring(0, jarEntryName.lastIndexOf(".")).replace(".", "/");
                                    doAddClass(classSet, classname);
                                }
                            }
                        }
                    }
                }
            }
        } catch (IOException e) {
            log.error("fail to get class set：", e);
            throw new RuntimeException(e);
        }
        return classSet;
    }


    /**
     * 加载类集合 -> Set<Class>
     * @param classSet 类集合
     * @param packagePath 包路径
     * @param packageName 包名
     */
    public static void addClass(Set<Class<?>> classSet, String packagePath, String packageName) {
        File[] files = new File(packagePath).listFiles(new FileFilter() {
            // 文件过滤
            public boolean accept(File file) { // 是类文件或者是文件夹
                return (file.isFile() && file.getName().endsWith(".class")) || file.isDirectory();
            }
        });

        for (File file : files) {
            String fileName = file.getName();
            if (file.isFile()) {
                String classname = fileName.substring(0, fileName.indexOf("."));
                if (StringUtils.isNotEmpty(packageName)) {
                    classname = packageName + "." + classname;
                }
                doAddClass(classSet, classname);
            } else {
                String subPackagePath = fileName;
                if (StringUtils.isNotEmpty(packagePath)) {
                    subPackagePath = packagePath + "/" + subPackagePath;
                }

                String subPackageName = fileName;
                if (StringUtils.isNotEmpty(packageName)) {
                    subPackageName = packageName + "." + subPackageName;
                }
                // 递归遍历文件夹
                addClass(classSet, subPackagePath, subPackageName);
            }
        }
    }


    /**
     * 加载类 -> Set<Class>
     * @param classSet 类集合
     * @param classname 加载的类名
     */
    public static void doAddClass(Set<Class<?>> classSet, String classname) {
        classSet.add(loadClass(classname, false));
    }
}
