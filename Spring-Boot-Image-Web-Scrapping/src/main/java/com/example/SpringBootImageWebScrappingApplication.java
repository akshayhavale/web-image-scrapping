package com.example;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.List;
import java.util.UUID;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

@SpringBootApplication
public class SpringBootImageWebScrappingApplication {

	private static final String URL = "https://www.dropbox.com/s/55mvidclz2lmzpf/RabbitToy.jpg?dl=0";

	public static void main(String[] args) {
		SpringApplication.run(SpringBootImageWebScrappingApplication.class, args);

		WebClient client = new WebClient();

		client.getOptions().setJavaScriptEnabled(false);
		client.getOptions().setCssEnabled(false);
		client.getOptions().setUseInsecureSSL(true);

		try {

			HtmlPage page = client.getPage(URL);
			final List<?> images = page.getByXPath("//img");
			for (Object imageObject : images) {
				HtmlImage image = (HtmlImage) imageObject;
				System.out.println(image.getSrcAttribute());
				URL url = null;
				try {
					url = new URL(image.getSrcAttribute());
				} catch (Exception e) {
					e.printStackTrace();
				}
				if (url == null) {
					continue;
				}
				InputStream in = new BufferedInputStream(url.openStream());
				ByteArrayOutputStream out = new ByteArrayOutputStream();
				byte[] buf = new byte[1024];
				int n = 0;
				while (-1 != (n = in.read(buf))) {
					out.write(buf, 0, n);
				}
				out.close();
				in.close();
				byte[] response = out.toByteArray();
				UUID uuid = UUID.randomUUID();
				FileOutputStream fos = new FileOutputStream(
						"/Users/akshayhavale/Downloads/ScrappedImages/" + uuid + ".jpeg");
				fos.write(response);
				fos.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
