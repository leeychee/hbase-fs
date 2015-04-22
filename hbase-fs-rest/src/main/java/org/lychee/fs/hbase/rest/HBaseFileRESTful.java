package org.lychee.fs.hbase.rest;

import com.sun.jersey.core.header.FormDataContentDisposition;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import org.apache.commons.io.FileUtils;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.lychee.fs.hbase.HBaseFile;
import org.lychee.fs.hbase.HBaseFileInputStream;
import org.lychee.fs.hbase.HBaseFileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Root resource (exposed at "file" path)
 */
@Path("file")
public class HBaseFileRESTful {

	private static final Logger log = LoggerFactory.getLogger(HBaseFileRESTful.class);

	/**
	 *
	 * @param uploadedInputStream
	 * @param fileDetail
	 * @return
	 */
	@POST
	@Path("/upload")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces(MediaType.TEXT_PLAIN)
	public Response uploadFile(
			@FormDataParam("file") InputStream uploadedInputStream,
			@FormDataParam("file") FormDataContentDisposition fileDetail) {

		File tmpFile = new File(FileUtils.getTempDirectoryPath() 
				+ File.separator + "hfs_upload" 
				+ File.separator + fileDetail.getFileName());

		try (InputStream uis = uploadedInputStream) {
			FileUtils.copyInputStreamToFile(uis, tmpFile);
		} catch (IOException ex) {
			return Response.serverError().entity("Fail to save file.").build();
		}
		String identifier;
		try {
			identifier = HBaseFileUtils.upload(tmpFile);
		} catch (IOException ioe) {
			return Response.serverError().entity("Fail to upload file.").build();
		}
		log.info("Success to upload file: {}, identifier: {}",
				fileDetail.getFileName(), identifier);
		return Response.ok().entity(identifier)
				.header("Access-Control-Allow-Origin", "*")
				.header("Access-Control-Allow-Methods", "POST, PUT")
				.build();
	}

	/**
	 * Method handling HTTP GET requests. The returned object will be sent to
	 * the client as "text/plain" media type.
	 *
	 * @param identifier
	 * @return String that will be returned as a text/plain response.
	 */
	@GET
	@Path("{identifier}")
	@Produces(MediaType.APPLICATION_OCTET_STREAM)
	public Response getFileByIdentifier(@PathParam("identifier") String identifier) {
		ResponseBuilder response;
		File file = new File(FileUtils.getTempDirectory() + File.separator + "downloads");
		try {
			HBaseFile hbf = HBaseFile.Factory.buildHBaseFile(identifier);
			InputStream hfis = new HBaseFileInputStream(hbf);
			response = Response.ok().entity(hfis)
					.header("Content-Disposition", "attachment; filename=\"" + hbf.getDesc() + "\"");
		} catch (FileNotFoundException ex) {
			response = Response.noContent();
			log.warn("File not exist: " + identifier);
		} catch (Exception e) {
			response = Response.serverError();
			log.error("Unkonwn error.", e);
		}
		return response.build();
	}

	@DELETE
	@Path("{identifier}")
	@Produces(MediaType.TEXT_PLAIN)
	public Response deleteFileByIdentifier(@PathParam("identifier") String identifier) {
		try {
			HBaseFile hbf = HBaseFile.Factory.buildHBaseFile(identifier);
			if (!hbf.exists())
				return Response.noContent().build();
			hbf.delete();
		} catch (IOException ex) {
			return Response.serverError().entity("Fail to delete file: " + identifier).build();
		}
		return Response.ok("Success to delete: " + identifier).build();
	}
}
