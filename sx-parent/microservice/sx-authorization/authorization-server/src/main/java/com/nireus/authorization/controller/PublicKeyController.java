package com.nireus.authorization.controller;

import java.security.KeyPair;
import java.security.interfaces.RSAPublicKey;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;

/**
 * 
 * <p>
 * 非对称加密公钥获取
 * </p>
 * 
 * @author wushixiong
 * @since 2021-05-26 15:10:07
 */
@RestController
@RequestMapping("/oauth")
public class PublicKeyController {

	@Autowired
	private KeyPair keyPair;

	@GetMapping("/getPublicKey")
	public Map<String, Object> getPublicKey() {
		RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
		RSAKey key = new RSAKey.Builder(publicKey).build();
		return new JWKSet(key).toJSONObject();
	}
}
