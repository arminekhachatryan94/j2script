package j2script;

import j2script.TokenizerException;
import j2script.ParserException;
import j2script.TypeErrorException;

import org.junit.Test;

import java.util.List;
import java.util.ArrayList;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.File;

public class ProgramTest {
    private String[] getPrograms() {
        File inputDirectory = new File("./testPrograms");
        return inputDirectory.list();
    }

    @Test
    public void runProgram() throws FileNotFoundException, ParserException, TokenizerException,
                                    TypeErrorException, IOException {
        main.main(getPrograms());
    }
}