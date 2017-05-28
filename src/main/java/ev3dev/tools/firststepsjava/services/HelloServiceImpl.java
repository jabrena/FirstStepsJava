package ev3dev.tools.firststepsjava.services;

import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import ev3dev.tools.firststepsjava.exceptions.InvalidParametersException;
import reactor.core.publisher.Mono;

import javax.tools.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.function.Function;

public class HelloServiceImpl implements HelloService{
    private static final String EMPTY = "";
    private static final String BAD_PARAMETERS = "bad parameters";

    @Override
    public Function<Mono<String>, Mono<String>> getGreetings() {
        return value -> value.flatMap(name -> {
            if (name.equals(EMPTY)) {
                return Mono.error(new InvalidParametersException(BAD_PARAMETERS));
            }
            return Mono.just(name);
        });
    }

    @Override
    public Function<Mono<String>, Mono<String>> decode() {
        return stringMono -> stringMono.flatMap(s -> Mono.just(new String(Base64.decode(s))));
                //.onErrorMap(Mono.error(new InvalidParametersException("No me gusta tu rollito")));
    }

    @Override
    public Function<Mono<String>, Mono<String>> compile() {

        return value -> value.flatMap(name -> {

            System.out.println(name);

            //Detect className
            final String pattern ="public class";
            final int pattern1 = name.indexOf("public class");
            System.out.println(pattern1);
            final int pattern2 = name.indexOf("{", pattern1);
            System.out.println(pattern2);
            final String className = name.substring(pattern1+pattern.length(),pattern2);
            System.out.println(className.trim());

            final String JAVA_IO_TEMPDIR = System.getProperty("java.io.tmpdir");
            System.out.println(JAVA_IO_TEMPDIR);
            File root = new File(JAVA_IO_TEMPDIR + "/java");

            boolean resultCompilation = false;

            try {

                File sourceFile = new File(root, className.trim() + ".java");
                sourceFile.getParentFile().mkdirs();
                Files.write(sourceFile.toPath(), name.getBytes(StandardCharsets.UTF_8));

// Compile source file.
                //JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
                //compiler.run(null, null, null, sourceFile.getPath());

                resultCompilation = javaCompile(sourceFile.getPath());

// Load and instantiate compiled class.
/*
                URLClassLoader classLoader = URLClassLoader.newInstance(new URL[]{root.toURI().toURL()});
                Class<?> cls = Class.forName("HelloWorld", true, classLoader); // Should print "hello".
                Object instance = cls.newInstance(); // Should print "world".
                System.out.println(instance); // Should print "test.Test@hashcode".
                final String result = (String) cls.getDeclaredMethod("main", new Class[] { String[].class }).invoke(null, new Object[] { null });
*/
            }catch(IOException e){
                e.printStackTrace();
                /*
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
                */
            }

            if(resultCompilation){
                return Mono.just(className.trim());
            }

            return Mono.just("ERROR");
        });
    }

    public boolean javaCompile(String fileName) throws IOException {
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
//      DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<JavaFileObject>();
        StandardJavaFileManager fileManager = compiler.getStandardFileManager(null/*diagnostics*/, null, null);
        Iterable<? extends JavaFileObject> compilationUnits = fileManager
                .getJavaFileObjectsFromStrings(Arrays.asList(fileName));
        JavaCompiler.CompilationTask task = compiler.getTask(null, fileManager, null/*diagnostics*/, null, null, compilationUnits);
        boolean success = task.call();
        fileManager.close();
        System.out.println("Success: " + success);
        return success;
    }

    @Override
    public Function<Mono<String>, Mono<String>> run() {

        //Execute bytecode
        final String JAVA_IO_TEMPDIR = System.getProperty("java.io.tmpdir");
        //System.out.println(JAVA_IO_TEMPDIR);
        File root = new File(JAVA_IO_TEMPDIR + "/java"); // On Windows running on C:\, this is C:\java.

        String pathJava = root.getAbsolutePath();
        System.out.println(pathJava);

        return value -> value.flatMap(name -> {
            System.out.println(name);
            if(!name.equals("ERROR")){

                final String resultShell = Shell.execute("java -classpath " + pathJava + " " + name);
                //System.out.println("demo: " + resultShell);

                //Shell.execute("ls " + pathJava);
                //Shell.execute("rm " + pathJava + "/*");

                return Mono.just(resultShell);

            }else {

                final String errorOutput = showErrors2(pathJava);

                return Mono.just(errorOutput);
            }
        });

    }

    private String showErrors(String pathJava){
        //Shell.execute("rm " + pathJava + "/err.txt");
        final String command = "javac " + pathJava + "/HelloWorld.java > " + pathJava + "/output.txt 2> " + pathJava + "/err.txt";
        System.out.println(command);
        final String resultShell = Shell.execute(command);
        System.out.println("cat " + pathJava + "/err.txt");
        final String errorOutput = Shell.execute("cat " + pathJava + "/err.txt");
        System.out.println(errorOutput);
        //final String commandRM = "rm -f " + pathJava + "/*.*";
        //Shell.execute(commandRM);

        return errorOutput;
    }

    private String showErrors2(String pathJava){

        final String JAVA_IO_TEMPDIR = System.getProperty("java.io.tmpdir");
        //System.out.println(JAVA_IO_TEMPDIR);
        File root = new File(JAVA_IO_TEMPDIR + "/java"); // On Windows running on C:\, this is C:\java.

        File sourceFile = new File(root, "HelloWorld.java");
        sourceFile.getParentFile().mkdirs();
        OutputStream output = new OutputStream()
        {
            private StringBuilder string = new StringBuilder();
            @Override
            public void write(int b) throws IOException {
                this.string.append((char) b );
            }

            public String toString(){
                return this.string.toString();
            }
        };
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        compiler.run(null, null, output, sourceFile.getPath());

        System.out.println(output.toString());


        return output.toString();

    }
}


