/*
 * OBIMP4J - Java OBIMP Lib
 * Copyright (C) 2013 alex_xpert
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.obimp.data.type;

import com.obimp.data.DataType;

/**
 * OctaWord – unsigned 16 bytes
 * @author alex_xpert
 */
public class OctaWord extends DataType {
    private int length = 16;
    private byte[] data = new byte[length];

    public OctaWord(int _data) {
        String d = String.valueOf(_data).replace("0x", "");
        String[] b = {new String(d.substring(0, 2)), new String(d.substring(2, 4)),
            new String(d.substring(4, 6)), new String(d.substring(6, 8)),
            new String(d.substring(8, 10)), new String(d.substring(10, 12)),
            new String(d.substring(12, 14)), new String(d.substring(14, 16)),
            new String(d.substring(16, 18)), new String(d.substring(18, 20)),
            new String(d.substring(20, 22)), new String(d.substring(22, 24)),
            new String(d.substring(24, 26)), new String(d.substring(26, 28)),
            new String(d.substring(28, 30)), new String(d.substring(30))};
        for(int i=0;i<length;i++) {
            this.data[i] = java.lang.Byte.valueOf(b[i]);
        }
    }
    
    public OctaWord(byte[] _data) {
        this.data = _data;
    }
    
    @Override
    public int getLenght() {
        return this.length;
    }

    @Override
    public byte[] getData() {
        return this.data;
    }

}
