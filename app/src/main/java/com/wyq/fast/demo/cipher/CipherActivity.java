/**
 * MIT License
 * <p>
 * Copyright (c) 2019 wangyognqi
 * <p>
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * <p>
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * <p>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.wyq.fast.demo.cipher;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.wyq.fast.core.cipher.Cipher;
import com.wyq.fast.core.cipher.CipherFactory;
import com.wyq.fast.demo.BaseAppCompatActivity;
import com.wyq.fast.demo.R;
import com.wyq.fast.utils.ViewBindUtil;
import com.wyq.fast.utils.ViewBindUtil.BindView;

/**
 * Author: WangYongQi
 * Cipher demo
 */

public class CipherActivity extends BaseAppCompatActivity {

    // 使用OpenSSL生成的RSA公钥 （可以用来加密）
    public final static String rsaPublicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDb5M+vLXnxBYJVkqdYIqdmVjER" +
            "gPKs6rdfDSwDIeXQCMfkpq7A3nyADxBsQTPdTk2f7jEs1ZSvG23jCQm85opEMuYJ" +
            "k5nky9PQE7dRE4dgN8Fwud9l1ktUMAvZCl5tumZpdTObhwcHOj1x966R/hL5J0Xs" +
            "WMd2+YtcJFcA6Dj78QIDAQAB";

    // 使用OpenSSL生成的RSA私钥 （可以用来解密）
    public final static String rsaPrivateKey = "MIICXAIBAAKBgQDb5M+vLXnxBYJVkqdYIqdmVjERgPKs6rdfDSwDIeXQCMfkpq7A" +
            "3nyADxBsQTPdTk2f7jEs1ZSvG23jCQm85opEMuYJk5nky9PQE7dRE4dgN8Fwud9l" +
            "1ktUMAvZCl5tumZpdTObhwcHOj1x966R/hL5J0XsWMd2+YtcJFcA6Dj78QIDAQAB" +
            "AoGAJy0Qw0j63LH6knxaTB2AbX8VpuSuV5hJqymRmc1EQFgQJPB9WedJjxcf+elz" +
            "8A2vTrsFD9PNdZURcs19aqY4I7GkwMoBVzo3qrPl3pWyeQrfRgSA9WfHJcJ+JtU6" +
            "Inszah2vX2EJ5nU5ffQCRq2XNi/pdegATQ72Sg2kzM3UvEECQQD9ThRs+FKZ1931" +
            "gOri1VbGJxVqRR6q9/Z/hv3O/Xmo8do+7CztvV61tngJ1mXBADqG0HXXSZo55ZN1" +
            "B1Ddn7DVAkEA3ju68cnMhrpTNn5CgVuA3QezXi76PTRfq7KFCQRY5TZZsK1nv8Ky" +
            "cnY+TZvzaTu0vFsGZD0dk3XgdAzksTKMrQJBAL87ZDZEXu/gRaSjuDCZyVH9jbBW" +
            "EGoYm5L5kcHkISYHuLGKUf0F22uqHwOmRdmoV4cR9/UZjwKorGNMq6M1fSUCQAW0" +
            "Hr4p4bfrfEs/vlE3mf0SP4ZBSmbbu0Da2hM7K1TmqjKgoLMJOi853at8PykKQT88" +
            "TtOvxdyM/++6jPwHQhkCQBcJ6BlR6M4pvwAaIM986HSvnu+JpAvXl9rel1lnF4Qt" +
            "msUrInjPDb0KRqGkAwjMywBrTwIes55T00FJSiYjHG0=";

    private static final String[] bits = {"128位", "192位", "256位"};
    private static final String[] encodings = {"utf-8", "gb2312", "gbk", "gb18030"};
    private static final String[] outputs = {"hex", "base64"};
    private static final String[] types = {"AES加密", "DES加密", "3DES加密", "RSA加密"};

    private int bit = 128;
    private String encoding = "utf-8";
    private Cipher.OutputType output = Cipher.OutputType.HEX;
    private Cipher.EncryptType encryptType = Cipher.EncryptType.AES;

    @BindView(R.id.etPassword)
    private EditText etPassword;
    @BindView(R.id.etOriginalData)
    private EditText etOriginalData;
    @BindView(R.id.tvEncryptData)
    private TextView tvEncryptData;
    @BindView(R.id.tvDecryptData)
    private TextView tvDecryptData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cipher);
        ViewBindUtil.bind(this);

        setSpinner(bits, R.id.spinnerBit);
        setSpinner(encodings, R.id.spinnerEncoding);
        setSpinner(outputs, R.id.spinnerOutput);
        setSpinner(types, R.id.spinnerType);
    }

    /**
     * 设置下拉框
     *
     * @param data
     * @param resId
     */
    private void setSpinner(String[] data, int resId) {
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, data);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner spinner = findViewById(resId);
        spinner.setAdapter(adapter);
        //添加事件Spinner事件监听
        spinner.setOnItemSelectedListener(new SpinnerSelectedListener(data, resId));
    }

    @ViewBindUtil.OnClick({R.id.btnEncrypt, R.id.btnDecrypt})
    public void onClick(View view) {
        String result;
        CipherFactory.Options options = CipherFactory.Options.getDefaultOptions();
        options.bit = bit;
        options.encoding = encoding;
        options.output = output;
        switch (view.getId()) {
            case R.id.btnEncrypt:
                // 加密
                tvEncryptData.setText("");
                tvDecryptData.setText("");
                if (encryptType == Cipher.EncryptType.RSA) {
                    // 这里使用RSA公钥加密
                    result = CipherFactory.create(encryptType).encryptString(rsaPublicKey, etOriginalData.getText().toString(), options);
                } else {
                    result = CipherFactory.create(encryptType).encryptString(etPassword.getText().toString(), etOriginalData.getText().toString(), options);
                }
                tvEncryptData.setText(result);
                break;
            case R.id.btnDecrypt:
                // 解密
                if (encryptType == Cipher.EncryptType.RSA) {
                    // 这里使用RSA私钥解密
                    result = CipherFactory.create(encryptType).decryptString(rsaPrivateKey, tvEncryptData.getText().toString(), options);
                } else {
                    result = CipherFactory.create(encryptType).decryptString(etPassword.getText().toString(), tvEncryptData.getText().toString(), options);
                }
                tvDecryptData.setText(result);
                break;
        }
    }

    /**
     * 下拉框按钮点击事件
     */
    class SpinnerSelectedListener implements AdapterView.OnItemSelectedListener {

        int resId;
        String[] array;

        public SpinnerSelectedListener(String[] data, int resId) {
            this.array = data;
            this.resId = resId;
        }

        public void onItemSelected(AdapterView<?> arg0, View view, int position, long arg3) {
            if (view instanceof TextView) {
                TextView textView = (TextView) view;
                textView.setTextColor(getContext().getResources().getColor(R.color.colorWhite));
            }
            switch (resId) {
                case R.id.spinnerBit:
                    String tempBit = array[position];
                    switch (tempBit) {
                        case "128位":
                            bit = 128;
                            break;
                        case "192位":
                            bit = 192;
                            break;
                        case "256位":
                            bit = 256;
                            break;
                    }
                    break;
                case R.id.spinnerEncoding:
                    encoding = array[position];
                    break;
                case R.id.spinnerOutput:
                    String tempOutput = array[position];
                    switch (tempOutput) {
                        case "hex":
                            output = Cipher.OutputType.HEX;
                            break;
                        case "base64":
                            output = Cipher.OutputType.BASE64;
                            break;
                    }
                    break;
                case R.id.spinnerType:
                    String tempType = array[position];
                    switch (tempType) {
                        case "AES加密":
                            encryptType = Cipher.EncryptType.AES;
                            break;
                        case "DES加密":
                            encryptType = Cipher.EncryptType.DES;
                            break;
                        case "3DES加密":
                            encryptType = Cipher.EncryptType.DES3;
                            break;
                        case "RSA加密":
                            encryptType = Cipher.EncryptType.RSA;
                            break;
                    }
                    break;
            }
        }

        public void onNothingSelected(AdapterView<?> arg0) {
        }
    }

}
