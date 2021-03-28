/*
 * Sonatype Nexus (TM) Open Source Version
 * Copyright (c) 2008-present Sonatype, Inc.
 * All rights reserved. Includes the third-party code listed at http://links.sonatype.com/products/nexus/oss/attributions.
 *
 * This program and the accompanying materials are made available under the terms of the Eclipse Public License Version 1.0,
 * which accompanies this distribution and is available at http://www.eclipse.org/legal/epl-v10.html.
 *
 * Sonatype Nexus (TM) Professional Version is available from Sonatype, Inc. "Sonatype" and "Sonatype Nexus" are trademarks
 * of Sonatype, Inc. Apache Maven is a trademark of the Apache Software Foundation. M2eclipse is a trademark of the
 * Eclipse Foundation. All other trademarks are the property of their respective owners.
 */
package org.sonatype.nexus.blobstore.s3.internal

import javax.annotation.Nullable

import org.sonatype.nexus.blobstore.api.BlobId
import org.sonatype.nexus.blobstore.api.BlobStoreException

import com.amazonaws.services.s3.model.AmazonS3Exception

import static java.lang.String.format

/**
 * A {@link BlobStoreException} specific to the S3 implementation
 *
 * @since 3.19
 */
class S3BlobStoreException
    extends BlobStoreException
{
  public static final DEFAULT_MESSAGE = 'An unexpected S3 error occurred. Check the logs for more details.'

  public static final INSUFFICIENT_PERM_CREATE_BUCKET_ERR_MSG = 'Insufficient permissions to create bucket.'

  public static final UNEXPECTED_ERR = 'An unexpected error occurred %s. Check the logs for more details.'

  public static final BUCKET_OWNERSHIP_ERR_MSG = 'Bucket exists but is not owned by you.'

  public static final ACCESS_DENIED_CODE = 'AccessDenied'

  public static final INVALID_ACCESS_KEY_ID_CODE = 'InvalidAccessKeyId'

  public static final SIGNATURE_DOES_NOT_MATCH_CODE = 'SignatureDoesNotMatch'

  public static Map<String, String> ERROR_CODE_MESSAGES = [
      (INVALID_ACCESS_KEY_ID_CODE)   : 'The Access Key ID provided was invalid.',
      (ACCESS_DENIED_CODE)           : 'Access denied. Please check the credentials provided have proper permissions.',
      (SIGNATURE_DOES_NOT_MATCH_CODE): 'The secret access key does not match causing an invalid signature.'
  ].withDefault { DEFAULT_MESSAGE }

  private final String message

  private S3BlobStoreException(final String message,
                               final Throwable cause,
                               @Nullable final BlobId blobId)
  {
    super(message, cause, blobId)
    this.message = message
  }

  private S3BlobStoreException(final String message) {
    super(message, null)
    this.message = message
  }

  static S3BlobStoreException buildException(final AmazonS3Exception cause) {
    new S3BlobStoreException(ERROR_CODE_MESSAGES[cause.errorCode], cause, null)
  }

  static S3BlobStoreException insufficientCreatePermissionsError() {
    new S3BlobStoreException(INSUFFICIENT_PERM_CREATE_BUCKET_ERR_MSG)
  }

  static S3BlobStoreException unexpectedError(String action) {
    new S3BlobStoreException(format(UNEXPECTED_ERR, action))
  }

  static S3BlobStoreException bucketOwnershipError() {
    new S3BlobStoreException(BUCKET_OWNERSHIP_ERR_MSG)
  }

  @Override
  String getMessage() {
    message
  }
}
