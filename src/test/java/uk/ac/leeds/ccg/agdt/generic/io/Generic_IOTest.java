/*
 * Copyright 2019 Andy Turner, University of Leeds.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package uk.ac.leeds.ccg.agdt.generic.io;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import uk.ac.leeds.ccg.agdt.generic.core.Generic_Environment;
import uk.ac.leeds.ccg.agdt.generic.core.Generic_Strings;

/**
 * To test all the functionality of Generic_IO.
 *
 * @author Andy Turner
 * @version 1.0.0
 */
public class Generic_IOTest {

    static Generic_Environment env;
    int logID;

    public Generic_IOTest() {
    }

    @BeforeAll
    public static void setUpClass() {
        try {
            Generic_Files files = new Generic_Files(new Generic_Defaults(
                    Paths.get(System.getProperty("user.home"), 
                            Generic_Strings.s_data,
                            Generic_Strings.s_generic)));
            env = new Generic_Environment(files);
        } catch (Exception ex) {
            ex.printStackTrace(System.err);
        }
    }

    @AfterAll
    public static void tearDownClass() {
    }

    @BeforeEach
    public void setUp() {
        try {
            logID = env.initLog(this.getClass().getSimpleName());
        } catch (Exception ex) {
            ex.printStackTrace(System.err);
        }
    }

    @AfterEach
    public void tearDown() {
    }

    /**
     * Test of writeObject method, of class Generic_IO.
     */
    @Test
    public void testWriteObject_Object_File() {
        try {
            env.log("writeObject", logID);
            String prefix = "Generic_Files";
            String suffix = Generic_Strings.symbol_dot + Generic_Strings.s_dat;
            Path f = null;
            try {
                f = getTestFile(prefix, suffix);
            } catch (IOException ex) {
                Logger.getLogger(Generic_IOTest.class.getName()).log(Level.SEVERE, null, ex);
            }
            env.log("Test file for env.files " + f);
            Generic_IO instance = env.io;
            instance.writeObject(env.files, f);
            // Make sure it is a new file
            f = getNewTestFile(prefix, suffix);
            env.log("Test file for env.files " + f);
            instance.writeObject(env.files, f);
        } catch (IOException ex) {
            ex.printStackTrace(System.err);
            env.log(ex.getMessage());
        }
    }

    public Path getNewTestFile(String prefix, String suffix) throws IOException {
        return Paths.get(env.files.getGeneratedDir().toString(), prefix + suffix);
    }

    public Path getTestFile(String prefix, String suffix) throws IOException {
        return env.io.createNewFile(env.files.getGeneratedDir(), prefix, suffix);
    }

    /**
     * Test of getFilePathLength method, of class Generic_IO.
     *
     * @throws java.io.IOException If encountered.
     */
    @Test
    public void testGetFilePathLength_File() throws IOException {
        env.log("getFilePathLength", logID);
        //System.out.println("getFilePathLength");
        Path f = env.files.getGeneratedDir();
        Generic_IO instance = env.io;
        int limit = 100;
        int result = instance.getFilePathLength(f);
        System.out.println(result);
        Assertions.assertTrue(result < limit);
    }

    /**
     * Test of getNumericallyOrderedFiles method, of class Generic_IO.
     */
    @Test
    public void testCreateNewFile() {
        env.log("createNewFile", logID);
        try {
            Path dir = env.files.getGeneratedDir();
            String prefix = "test";
            String suffix = ".dat";
            Generic_IO instance = env.io;
            Path f = instance.createNewFile(dir, prefix, suffix);
            Assertions.assertTrue(Files.exists(f));
        } catch (IOException ex) {
            ex.printStackTrace(System.err);
            env.log(ex.getMessage());
        }
    }

}
