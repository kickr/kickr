/*
 * The MIT License
 *
 * Copyright 2015 nikku.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package kickr.web.api;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import kickr.db.entity.Player;
import kickr.web.model.PlayerData;

/**
 *
 * @author nikku
 */
public class PlayerUtil {


  public static Pattern CREATE_PLAYER_PATTERN =
                    Pattern.compile("^(\\w+)\\s*(<([\\w@.]+)>)?$");

  /**
   * Parse a player from a <code>PATTERN&lt;email&gt;</code> string.
   *
   * @param str
   * @return
   */
  public static PlayerData parsePlayer(String str) {

    if (str == null) {
      return null;
    }
    
    Matcher matcher = CREATE_PLAYER_PATTERN.matcher(str);

    if (!matcher.matches()) {
      return null;
    }

    if (matcher.groupCount() > 3) {
      return new PlayerData(matcher.group(1), matcher.group(3));
    } else {
      return new PlayerData(matcher.group(1));
    }
  }
}