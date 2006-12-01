/*
 * Copyright 2006 Jeremias Maerki.
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

/* $Id: DataMatrixHighLevelEncodeTest.java,v 1.3 2006-12-01 15:22:43 jmaerki Exp $ */

package org.krysalis.barcode4j.impl.datamatrix;

import org.krysalis.barcode4j.tools.TestHelper;

import junit.framework.TestCase;

/**
 * Tests for the high-level encoder.
 * 
 * @version $Id: DataMatrixHighLevelEncodeTest.java,v 1.3 2006-12-01 15:22:43 jmaerki Exp $
 */
public class DataMatrixHighLevelEncodeTest extends TestCase {

    private static final boolean DEBUG = true;
    
    public void testASCIIEncodation() throws Exception {
        String visualized;

        visualized = encodeHighLevel("123456");
        assertEquals("142 164 186", visualized);

        visualized = encodeHighLevel("30Q324343430794<OQQ");
        assertEquals("160 82 162 173 173 173 137 224 61 80 82 82", visualized);
    }
    
    public void testC40Encodation() throws Exception {
        String visualized;

        visualized = encodeHighLevel("AIMAIMAIM");
        assertEquals("230 91 11 91 11 91 11 254", visualized);
        //230 shifts to C40 encodation, 254 unlatches

        visualized = encodeHighLevel("AIMAIAb");
        assertEquals("230 91 11 90 255 12 209 254", visualized);

        visualized = encodeHighLevel("AIMAIMAIMĖ");
        assertEquals("230 91 11 91 11 91 11 11 9 254", visualized);

        visualized = encodeHighLevel("AIMAIMAIMė");
        assertEquals("230 91 11 91 11 91 11 10 243 254 235 107", visualized);

        visualized = encodeHighLevel("A1B2C3D4E5F6G7H8I9J0K1L2");
        assertEquals("230 88 88 40 8 107 147 59 67 126 206 78 126 144 121 35 47 254", visualized);
    }

    public void testTextEncodation() throws Exception {
        String visualized;

        visualized = encodeHighLevel("aimaimaim");
        assertEquals("239 91 11 91 11 91 11 254", visualized);
        //239 shifts to Text encodation, 254 unlatches

        visualized = encodeHighLevel("aimaimaim'");
        assertEquals("239 91 11 91 11 91 11 7 49 254", visualized);

        visualized = encodeHighLevel("aimaimaIm");
        assertEquals("239 91 11 91 11 87 218 254 110", visualized);

        visualized = encodeHighLevel("aimaimaimB");
        assertEquals("239 91 11 91 11 91 11 12 209 254", visualized);
    }

    private String encodeHighLevel(String msg) {
        String encoded = DataMatrixHighLevelEncoder.encodeHighLevel(msg);
        String visualized = TestHelper.visualize(encoded);
        if (DEBUG) {
            System.out.println(msg + ": " + visualized);
        }
        return visualized;
    }
    
}
