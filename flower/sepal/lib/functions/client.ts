import { getAuthHeader } from "@/lib/functions/security-functions";
import { ApiResponse } from "@/types/apiTypes";

/**
 * Performs an HTTP GET request to the greenhouse api
 *
 * @param url endpoint url
 * @param params query params
 */
export async function get<T>(
  url: string,
  params: Record<string, string | number>,
): Promise<T> {
  const headers = getAuthHeader();
  headers["Content-Type"] = "application/json";

  let finalUrl = url;
  for (const [key, value] of Object.entries(params)) {
    finalUrl = finalUrl.replace(
      `{${key}}`,
      encodeURIComponent(value.toString()),
    );
  }

  const res = await fetch(finalUrl, {
    method: "GET",
    headers,
  });

  if (!res.ok) {
    throw new Error(
      `Failed to get from ${url} with params ${params} with status: ${res.status}`,
    );
  }

  const data: ApiResponse<T> = await res.json();
  if (!data.success) {
    throw new Error(
      `API call to ${url} with params: ${params} returned with error: ${data.message} || ${JSON.stringify(data)}`,
    );
  }

  return data.data;
}

/**
 * Performs an HTTP POST request to the greenhouse api
 *
 * @param url endpoint url
 * @param params query params
 * @param body request body
 */
export async function post<T>(
  url: string,
  params: Record<string, string | number>,
  body: any,
): Promise<T> {
  const headers = getAuthHeader();
  headers["Content-Type"] = "application/json";

  let finalUrl = url;
  for (const [key, value] of Object.entries(params)) {
    finalUrl = finalUrl.replace(
      `{${key}}`,
      encodeURIComponent(value.toString()),
    );
  }

  const res = await fetch(finalUrl, {
    method: "POST",
    headers,
    body: JSON.stringify(body),
  });

  if (!res.ok) {
    throw new Error(
      `Failed to post from ${url} with params ${params} with status: ${res.status}`,
    );
  }

  const data: ApiResponse<T> = await res.json();
  if (!data.success) {
    throw new Error(
      `API call to ${url} with params ${params} returned with error: ${data.message} || ${JSON.stringify(data)}`,
    );
  }

  return data.data;
}

/**
 * Performs an HTTP POST request to the greenhouse api with a file
 *
 * @param url endpoint url
 * @param params query params
 * @param formData form data file
 */
export async function postFile<T>(
  url: string,
  params: Record<string, string | number>,
  formData: any,
): Promise<T> {
  const headers = getAuthHeader();

  let finalUrl = url;
  for (const [key, value] of Object.entries(params)) {
    finalUrl = finalUrl.replace(
      `{${key}}`,
      encodeURIComponent(value.toString()),
    );
  }

  const res = await fetch(finalUrl, {
    method: "POST",
    headers,
    body: formData,
  });

  if (!res.ok) {
    throw new Error(
      `Failed to post from ${url} with params ${params} with status: ${res.status}`,
    );
  }

  const data: ApiResponse<T> = await res.json();
  if (!data.success) {
    throw new Error(
      `API call to ${url} with params: ${params} returned with error: ${data.message} || ${JSON.stringify(data)}`,
    );
  }

  return data.data;
}

/**
 * Performs an HTTP PUT request to the greenhouse api
 *
 * @param url endpoint url
 * @param params query params
 * @param body request body
 */
export async function put<T>(
  url: string,
  params: Record<string, string | number>,
  body: any,
): Promise<T> {
  const headers = getAuthHeader();
  headers["Content-Type"] = "application/json";

  let finalUrl = url;
  for (const [key, value] of Object.entries(params)) {
    finalUrl = finalUrl.replace(
      `{${key}}`,
      encodeURIComponent(value.toString()),
    );
  }

  const res = await fetch(finalUrl, {
    method: "PUT",
    headers,
    body: JSON.stringify(body),
  });

  if (!res.ok) {
    throw new Error(
      `Failed to put from ${url} with params ${params} with status: ${res.status}`,
    );
  }

  const data: ApiResponse<T> = await res.json();
  if (!data.success) {
    throw new Error(
      `API call to ${url} with params: ${params} returned with error: ${data.message} || ${JSON.stringify(data)}`,
    );
  }

  return data.data;
}

export async function del<T>(
  url: string,
  params: Record<string, string | number>,
): Promise<T> {
  const headers = getAuthHeader();
  headers["Content-Type"] = "application/json";

  let finalUrl = url;
  for (const [key, value] of Object.entries(params)) {
    finalUrl = finalUrl.replace(
      `{${key}}`,
      encodeURIComponent(value.toString()),
    );
  }

  const res = await fetch(finalUrl, {
    method: "DELETE",
    headers,
  });

  if (!res.ok) {
    throw new Error(
      `Failed to delete from ${url} with params ${params} with status: ${res.status}`,
    );
  }

  const data: ApiResponse<T> = await res.json();
  if (!data.success) {
    throw new Error(
      `API call to ${url} with params: ${params} returned with error: ${data.message} || ${JSON.stringify(data)}`,
    );
  }

  return data.data;
}
