/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package org.apache.hadoop.fs.adl.live;

import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.fs.adl.common.Parallelized;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.util.*;

/**
 * Test supported ASCII, UTF-8 character set supported by Adl storage file
 * system on file/folder operation.
 */
@RunWith(Parallelized.class)
public class TestAdlSupportedCharsetInPath {

  private static final String TEST_ROOT = "/test/";
  private static final Logger LOG = LoggerFactory
      .getLogger(TestAdlSupportedCharsetInPath.class);
  private String path;

  public TestAdlSupportedCharsetInPath(String filePath) {
    path = filePath;
  }

  @Parameterized.Parameters(name = "{0}")
  public static Collection<Object[]> adlCharTestData()
      throws UnsupportedEncodingException {

    ArrayList<String> filePathList = new ArrayList<>();
    for (int i = 32; i < 127; ++i) {
      String specialChar = (char) i + "";
      if (i >= 48 && i <= 57) {
        continue;
      }

      if (i >= 65 && i <= 90) {
        continue;
      }

      if (i >= 97 && i <= 122) {
        continue;
      }

      // Special char at start of the path
      if (i != 92 && i != 58 && i != 46 && i != 47) {
        filePathList.add(specialChar + "");
      }

      // Special char at end of string
      if (i != 92 && i != 47 && i != 58) {
        filePathList.add("file " + i + " " + specialChar);
      }

      // Special char in between string
      if (i != 47 && i != 58 && i != 92) {
        filePathList.add("file " + i + " " + specialChar + "_name");
      }
    }

    filePathList.add("a  ");
    filePathList.add("a..b");
    fillUnicodes(filePathList);
    Collection<Object[]> result = new ArrayList<>();
    for (String item : filePathList) {
      result.add(new Object[] {item});
    }
    return result;
  }

  private static void fillUnicodes(ArrayList<String> filePathList) {
    // Unicode characters
    filePathList.add("???????????????? ??????????????"); // Arabic
    filePathList.add("T?? dh??nat i madh"); // Albanian
    filePathList.add("?????? ??????????????????"); // Armenian
    filePathList.add("b??y??k data"); // Azerbaijani
    filePathList.add("?????????????? ????????????????"); // Belarusian,
    filePathList.add("????????? ????????????"); // Bengali
    filePathList.add("veliki podataka"); // Bosnian
    filePathList.add("???????????? ??????????"); // Bulgarian
    filePathList.add("?????????"); // Chinese - Simplified
    filePathList.add("?????????"); // Chinese - Traditional
    filePathList.add("???????????? ???????????????????????????"); // Georgian,
    filePathList.add("gro??e Daten"); // German
    filePathList.add("???????????? ????????????????"); // Greek
    filePathList.add("???????????? ??????????????????"); // Gujarati
    filePathList.add("???????????? ????????????"); // Hebrew
    filePathList.add("???????????? ????????????"); // Hindi
    filePathList.add("st??r g??gn"); // Icelandic
    filePathList.add("sonra?? m??r"); // Irish
    filePathList.add("??????????????????"); // Japanese
    filePathList.add("?????????? ????????????????"); // Kazakh
    filePathList.add("??????????????????????????????"); // Khmer
    filePathList.add("??? ?????????"); // Korean
    filePathList.add("?????????????????? ??????????????????????????????"); // Lao
    filePathList.add("???????????? ????????????????"); // Macedonian
    filePathList.add("???????????? ????????????"); // Nepali
    filePathList.add("???????????? ???????????????"); // Malayalam
    filePathList.add("???????????? ????????????"); // Marathi
    filePathList.add("?????? ????????????????"); // Mangolian
    filePathList.add("?????????????? ????????"); // Persian
    filePathList.add("???????????? ???????????? ?????????"); // Punjabi
    filePathList.add("?????????????? ????????????"); // Russian
    filePathList.add("???????????? ????????????????"); // Serbian
    filePathList.add("??????????????? ????????????"); // Sinhala
    filePathList.add("big d??t"); // Slovak
    filePathList.add("?????????????????? ??????????"); // Tajik
    filePathList.add("??????????????? ????????????"); // Tamil
    filePathList.add("??????????????? ????????????"); // Telugu
    filePathList.add("??????????????????????????????"); // Thai
    filePathList.add("b??y??k veri"); // Turkish
    filePathList.add("???????????? ????????"); // Ukranian
    filePathList.add("?????? ?????????? ?? ????????"); // Urdu
    filePathList.add("katta ma'lumotlar"); // Uzbek
    filePathList.add("d??? li???u l???n"); // Vietanamese
    filePathList.add("?????????? ??????????"); // Yiddish
    filePathList.add("big idatha"); // Zulu
    filePathList.add("rachel??");
    filePathList.add("jessica??");
    filePathList.add("sarah??");
    filePathList.add("katie??");
    filePathList.add("wendy??");
    filePathList.add("david??");
    filePathList.add("priscilla??");
    filePathList.add("oscar??");
    filePathList.add("xavier??");
    filePathList.add("gabriella??");
    filePathList.add("david??");
    filePathList.add("irene??");
    filePathList.add("fred??");
    filePathList.add("david??");
    filePathList.add("ulysses??");
    filePathList.add("gabriella??");
    filePathList.add("zach??");
    filePathList.add("gabriella??");
    filePathList.add("ulysses??");
    filePathList.add("david??");
    filePathList.add("sarah??");
    filePathList.add("holly??");
    filePathList.add("nick??");
    filePathList.add("ulysses??");
    filePathList.add("mike??");
    filePathList.add("priscilla??");
    filePathList.add("wendy??");
    filePathList.add("jessica??");
    filePathList.add("fred??");
    filePathList.add("fred??");
    filePathList.add("sarah??");
    filePathList.add("calvin??");
    filePathList.add("xavier??");
    filePathList.add("yuri??");
    filePathList.add("ethan??");
    filePathList.add("holly??");
    filePathList.add("xavier??");
    filePathList.add("victor??");
    filePathList.add("wendy??");
    filePathList.add("jessica??");
    filePathList.add("quinn??");
    filePathList.add("xavier??");
    filePathList.add("nick??");
    filePathList.add("rachel??");
    filePathList.add("oscar??");
    filePathList.add("zach??");
    filePathList.add("zach??");
    filePathList.add("rachel??");
    filePathList.add("jessica??");
    filePathList.add("luke??");
    filePathList.add("tom??");
    filePathList.add("nick??");
    filePathList.add("nick??");
    filePathList.add("ethan??");
    filePathList.add("fred??");
    filePathList.add("priscilla??");
    filePathList.add("zach??");
    filePathList.add("xavier??");
    filePathList.add("zach??");
    filePathList.add("ethan??");
    filePathList.add("oscar??");
    filePathList.add("irene??");
    filePathList.add("irene??");
    filePathList.add("victor??");
    filePathList.add("wendy??");
    filePathList.add("mike??");
    filePathList.add("fred??");
    filePathList.add("mike??");
    filePathList.add("sarah??");
    filePathList.add("quinn??");
    filePathList.add("mike??");
    filePathList.add("nick??");
    filePathList.add("nick??");
    filePathList.add("tom??");
    filePathList.add("bob??");
    filePathList.add("yuri??");
    filePathList.add("david??");
    filePathList.add("quinn??");
    filePathList.add("mike??");
    filePathList.add("david??");
    filePathList.add("ethan??");
    filePathList.add("nick??");
    filePathList.add("yuri??");
    filePathList.add("ethan??");
    filePathList.add("bob??");
    filePathList.add("david??");
    filePathList.add("priscilla??");
    filePathList.add("nick??");
    filePathList.add("luke??");
    filePathList.add("irene??");
    filePathList.add("xavier??");
    filePathList.add("fred??");
    filePathList.add("ulysses??");
    filePathList.add("wendy??");
    filePathList.add("zach??");
    filePathList.add("rachel??");
    filePathList.add("sarah??");
    filePathList.add("alice??");
    filePathList.add("bob??");
  }

  @AfterClass
  public static void testReport() throws IOException, URISyntaxException {
    if (!AdlStorageConfiguration.isContractTestEnabled()) {
      return;
    }

    FileSystem fs = AdlStorageConfiguration.createStorageConnector();
    fs.delete(new Path(TEST_ROOT), true);
  }

  @Test
  public void testAllowedSpecialCharactersMkdir()
      throws IOException, URISyntaxException {
    Path parentPath = new Path(TEST_ROOT, UUID.randomUUID().toString() + "/");
    Path specialFile = new Path(parentPath, path);
    FileSystem fs = AdlStorageConfiguration.createStorageConnector();

    Assert.assertTrue("Mkdir failed : " + specialFile, fs.mkdirs(specialFile));
    Assert.assertTrue("File not Found after Mkdir success" + specialFile,
        fs.exists(specialFile));
    Assert.assertTrue("Not listed under parent " + parentPath,
        contains(fs.listStatus(parentPath),
            fs.makeQualified(specialFile).toString()));
    Assert.assertTrue("Delete failed : " + specialFile,
            fs.delete(specialFile, true));
    Assert.assertFalse("File still exist after delete " + specialFile,
        fs.exists(specialFile));
  }

  private boolean contains(FileStatus[] statuses, String remotePath) {
    for (FileStatus status : statuses) {
      if (status.getPath().toString().equals(remotePath)) {
        return true;
      }
    }

    Arrays.stream(statuses).forEach(s -> LOG.info(s.getPath().toString()));
    return false;
  }

  @Before
  public void setup() throws Exception {
    org.junit.Assume
        .assumeTrue(AdlStorageConfiguration.isContractTestEnabled());
  }

  @Test
  public void testAllowedSpecialCharactersRename()
      throws IOException, URISyntaxException {

    String parentPath = TEST_ROOT + UUID.randomUUID().toString() + "/";
    Path specialFile = new Path(parentPath + path);
    Path anotherLocation = new Path(parentPath + UUID.randomUUID().toString());
    FileSystem fs = AdlStorageConfiguration.createStorageConnector();

    Assert.assertTrue("Could not create " + specialFile.toString(),
        fs.createNewFile(specialFile));
    Assert.assertTrue(
        "Failed to rename " + specialFile.toString() + " --> " + anotherLocation
            .toString(), fs.rename(specialFile, anotherLocation));
    Assert.assertFalse("File should not be present after successful rename : "
        + specialFile.toString(), fs.exists(specialFile));
    Assert.assertTrue("File should be present after successful rename : "
        + anotherLocation.toString(), fs.exists(anotherLocation));
    Assert.assertFalse(
        "Listed under parent whereas expected not listed : " + parentPath,
        contains(fs.listStatus(new Path(parentPath)),
            fs.makeQualified(specialFile).toString()));

    Assert.assertTrue(
        "Failed to rename " + anotherLocation.toString() + " --> " + specialFile
            .toString(), fs.rename(anotherLocation, specialFile));
    Assert.assertTrue(
        "File should be present after successful rename : " + "" + specialFile
            .toString(), fs.exists(specialFile));
    Assert.assertFalse("File should not be present after successful rename : "
        + anotherLocation.toString(), fs.exists(anotherLocation));

    Assert.assertTrue("Not listed under parent " + parentPath,
        contains(fs.listStatus(new Path(parentPath)),
            fs.makeQualified(specialFile).toString()));

    Assert.assertTrue("Failed to delete " + parentPath,
        fs.delete(new Path(parentPath), true));
  }
}