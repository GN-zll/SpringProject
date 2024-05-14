package org.springframework.beans.factory;

import org.springframework.beans.factory.annotation.Configuration;
import org.springframework.beans.factory.annotation.SpringTest;
import org.springframework.beans.factory.annotation.TestConfiguration;
import org.springframework.beans.factory.stereotype.Component;
import org.springframework.exceptions.ConfigurationsException;
import org.springframework.exceptions.SpringTestFileException;
import org.springframework.exceptions.TestConfigurationFileException;

import java.io.File;
import java.lang.annotation.Annotation;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FileScanner {
    public static ArrayList<Class<?>> getComponentFiles(String basePackage) throws URISyntaxException, ClassNotFoundException {
        ArrayList<Class<?>> componentFiles = new ArrayList<>();
        instantiate(componentFiles, basePackage, Component.class);
        return componentFiles;
    }

    public static Class<?> getSpringTestFile(String basePackage) throws URISyntaxException, ClassNotFoundException, SpringTestFileException {
        ArrayList<Class<?>> componentFiles = new ArrayList<>();
        instantiate(componentFiles, basePackage, SpringTest.class);

        if (componentFiles.size() != 1)
            throw new SpringTestFileException();

        return componentFiles.get(0);
    }

    public static Class<?> getTestConfiguration(String basePackage) throws URISyntaxException, ClassNotFoundException, TestConfigurationFileException {
        ArrayList<Class<?>> componentFiles = new ArrayList<>();
        instantiate(componentFiles, basePackage, TestConfiguration.class);

        if (componentFiles.size() != 1)
            throw new TestConfigurationFileException();

        return componentFiles.get(0);
    }

    private static void instantiate(List<Class<?>> componentFiles, String rootDirectoryName, Class<? extends Annotation> annotationClass) throws URISyntaxException, ClassNotFoundException {
        String rootDirectoryPath = rootDirectoryName.replace('.', '/');
        URL rootDirectoryURL = ClassLoader.getSystemClassLoader().getResource(rootDirectoryPath);
        File rootDirectory = new File(Objects.requireNonNull(rootDirectoryURL).toURI());

        searchFiles(componentFiles, rootDirectory, rootDirectoryName, annotationClass);
    }

    static Class<?> getConfigurations(String rootDirectoryName) throws URISyntaxException, ClassNotFoundException, ConfigurationsException {
        String rootDirectoryPath = rootDirectoryName.replace('.', '/');
        URL rootDirectoryURL = ClassLoader.getSystemClassLoader().getResource(rootDirectoryPath);
        File rootDirectory = new File(Objects.requireNonNull(rootDirectoryURL).toURI());
        ArrayList<Class<?>> configurationsFiles = new ArrayList<>();

        try {
            searchFiles(configurationsFiles, rootDirectory, rootDirectoryName, Configuration.class);

            if (configurationsFiles.isEmpty()) throw new ClassNotFoundException();
            if (configurationsFiles.size() > 1) throw new ConfigurationsException();

            System.out.println("conf " + configurationsFiles.get(0).toString());
            return configurationsFiles.get(0);
        } catch (ClassNotFoundException exception) {
            throw new ClassNotFoundException("Error! Project needs contain @Configuration file");
            // some code, doing without Configuration
        } catch (ConfigurationsException e) {
            throw new ConfigurationsException();
        }
    }

    private static void searchFiles(List<Class<?>> foundFiles, File currentDirectory, String rootDirectoryName, Class<? extends Annotation> annotationClass) throws ClassNotFoundException {
        File[] childFiles = currentDirectory.listFiles();

        for (var file : Objects.requireNonNull(childFiles)) {
            String path = file.getPath();
            if (path.endsWith(".class")) {
                String className = path.substring(path.indexOf(rootDirectoryName), path.lastIndexOf('.')).replace('/', '.');
                Class<?> classObject = Class.forName(className);

                if (classObject.isAnnotationPresent(annotationClass)) {
                    foundFiles.add(classObject);
                }
            } else {
                searchFiles(foundFiles, file, rootDirectoryName, annotationClass);
            }
        }
    }
}
