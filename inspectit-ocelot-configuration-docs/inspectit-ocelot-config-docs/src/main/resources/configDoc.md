inspectIT Ocelot Configuration Documentation
============================================
Scopes
------
### s_apacheclient_doExecute

### s_httpurlconnection_connect

### s_httpurlconnection_getInputStream

### s_httpurlconnection_getOutputStream

### s_httpurlconnection_requestInitiators

### s_jdbc_preparedstatement_execute

### s_jdbc_preparedstatement_executeBatch

### s_jdbc_statement_execute
Scope for all methods where JDBC statements are executed.
### s_jdbc_statement_executeBatch

### s_servletapi_filter_doFilter

### s_servletapi_servlet_service

### s_servletapi_servletresponse_getOutputStream

### s_servletapi_servletresponse_getWriter

Rules
-----
<a id="r_apacheclient_detect_entry"></a>
### r_apacheclient_detect_entry
Can be included to detect and exclude nested calls.

#### Attributes:

##### preEntry
- http_is_entry: [a_entrypoint_check](#a_entrypoint_check)
<a id="r_apacheclient_extract_details"></a>
### r_apacheclient_extract_details
Reads all HTTP related data, e.g. http_raw_path, http_path, http_method, etc. 
Designed to be applied on the scope s_apacheclient_doExecute.

#### Include:

- [r_apacheclient_detect_entry](#r_apacheclient_detect_entry)
- [r_http_parametrize_path](#r_http_parametrize_path)
#### Attributes:

##### entry
- http_method: [a_apacheclient_getMethod](#a_apacheclient_getMethod)
- http_path: [a_assign_value](#a_assign_value)
- http_raw_path: [a_apacheclient_getPath](#a_apacheclient_getPath)
- http_target_host: [a_apacheclient_toHostString](#a_apacheclient_toHostString)
##### exit
- http_is_error: [a_http_isErrorStatus](#a_http_isErrorStatus)
- http_status: [a_apacheclient_getStatus](#a_apacheclient_getStatus)
##### preEntry
- http_is_entry: [a_entrypoint_check](#a_entrypoint_check)
<a id="r_apacheclient_http_metric"></a>
### r_apacheclient_http_metric
Records the http/out metrics for calls done via the Apache HTTP Client.

#### Include:

- [r_apacheclient_extract_details](#r_apacheclient_extract_details)
- [r_http_client_record_metric_on_method](#r_http_client_record_metric_on_method)
#### Attributes:

##### entry
- capture_time_condition: [a_assign_true](#a_assign_true)
- http_method: [a_apacheclient_getMethod](#a_apacheclient_getMethod)
- http_path: [a_assign_value](#a_assign_value)
- http_raw_path: [a_apacheclient_getPath](#a_apacheclient_getPath)
- http_target_host: [a_apacheclient_toHostString](#a_apacheclient_toHostString)
- method_entry_time: [a_timing_nanos](#a_timing_nanos)
##### exit
- http_duration: [a_assign_value](#a_assign_value)
- http_is_error: [a_http_isErrorStatus](#a_http_isErrorStatus)
- http_status: [a_apacheclient_getStatus](#a_apacheclient_getStatus)
- method_duration: [a_timing_elapsedMillis](#a_timing_elapsedMillis)
##### preEntry
- http_is_entry: [a_entrypoint_check](#a_entrypoint_check)
<a id="r_apacheclient_propagation"></a>
### r_apacheclient_propagation
Performs up- and down-propagation via the HTTP Headers.

#### Include:

- [r_apacheclient_detect_entry](#r_apacheclient_detect_entry)
#### Attributes:

##### postEntry
- do_down_propagation: [a_apacheclient_downPropagation](#a_apacheclient_downPropagation)
##### preExit
- do_up_propagation: [a_apacheclient_upPropagation](#a_apacheclient_upPropagation)
##### preEntry
- http_is_entry: [a_entrypoint_check](#a_entrypoint_check)
<a id="r_apacheclient_servicegraph_propagation"></a>
### r_apacheclient_servicegraph_propagation
Reads and writes the service graph correlation information on the context (e.g. origin_service and target_service).

#### Include:

- [r_servicegraph_prepare_down_propagation](#r_servicegraph_prepare_down_propagation)
#### Attributes:

##### entry
- servicegraph_origin_service: [a_assign_value](#a_assign_value)
<a id="r_apacheclient_servicegraph_record"></a>
### r_apacheclient_servicegraph_record
Records an HTTP call done via the Apache HTTP Client as service-call.

#### Include:

- [r_servicegraph_outbound_record_method](#r_servicegraph_outbound_record_method)
- [r_apacheclient_detect_entry](#r_apacheclient_detect_entry)
- [r_apacheclient_extract_details](#r_apacheclient_extract_details)
#### Attributes:

##### entry
- capture_time_condition: [a_assign_true](#a_assign_true)
- http_method: [a_apacheclient_getMethod](#a_apacheclient_getMethod)
- http_path: [a_assign_value](#a_assign_value)
- http_raw_path: [a_apacheclient_getPath](#a_apacheclient_getPath)
- http_target_host: [a_apacheclient_toHostString](#a_apacheclient_toHostString)
- method_entry_time: [a_timing_nanos](#a_timing_nanos)
- servicegraph_is_entry: [a_entrypoint_check](#a_entrypoint_check)
- servicegraph_protocol: [a_assign_value](#a_assign_value)
##### exit
- http_is_error: [a_http_isErrorStatus](#a_http_isErrorStatus)
- http_status: [a_apacheclient_getStatus](#a_apacheclient_getStatus)
- method_duration: [a_timing_elapsedMillis](#a_timing_elapsedMillis)
- servicegraph_duration: [a_assign_value](#a_assign_value)
- servicegraph_is_error: [a_assign_value](#a_assign_value)
- servicegraph_target_external: [a_assign_value](#a_assign_value)
##### postExit
- servicegraph_target_service: [a_assign_null](#a_assign_null)
##### preEntry
- http_is_entry: [a_entrypoint_check](#a_entrypoint_check)
<a id="r_apacheclient_tracing"></a>
### r_apacheclient_tracing
Enables tracing of Apache HTTP Client calls.

#### Include:

- [r_apacheclient_extract_details](#r_apacheclient_extract_details)
- [r_http_tracing_span_name_default](#r_http_tracing_span_name_default)
- [r_http_client_tracing_default_attributes](#r_http_client_tracing_default_attributes)
#### Attributes:

##### entry
- http_method: [a_apacheclient_getMethod](#a_apacheclient_getMethod)
- http_path: [a_assign_value](#a_assign_value)
- http_raw_path: [a_apacheclient_getPath](#a_apacheclient_getPath)
- http_span_name: [a_string_concat_3](#a_string_concat_3)
- http_target_host: [a_apacheclient_toHostString](#a_apacheclient_toHostString)
- method_fqn: [a_method_getFQN](#a_method_getFQN)
##### exit
- http_is_error: [a_http_isErrorStatus](#a_http_isErrorStatus)
- http_status: [a_apacheclient_getStatus](#a_apacheclient_getStatus)
##### preEntry
- http_is_entry: [a_entrypoint_check](#a_entrypoint_check)
#### Tracing:

##### start-span: true

<a id="r_capture_method_duration"></a>
### r_capture_method_duration
When applied to a method, this rule will populate method_duration with the duration the method execution took.

#### Include:

- [r_capture_method_duration_conditional](#r_capture_method_duration_conditional)
#### Attributes:

##### entry
- capture_time_condition: [a_assign_true](#a_assign_true)
- method_entry_time: [a_timing_nanos](#a_timing_nanos)
##### exit
- method_duration: [a_timing_elapsedMillis](#a_timing_elapsedMillis)
<a id="r_capture_method_duration_conditional"></a>
### r_capture_method_duration_conditional
Conditionally captures the execution time of the current method into method_duration
The capturing will only happen it capture_time_condition is defined as true.
For example, http instrumentation define capture_time_condition based on http_is_entry
The condition is there to prevent unnecessary invocations of System.nanoTime(), which can be expensive

#### Include:

- [r_capture_method_entry_timestamp_conditional](#r_capture_method_entry_timestamp_conditional)
#### Attributes:

##### exit
- method_duration: [a_timing_elapsedMillis](#a_timing_elapsedMillis)
##### entry
- method_entry_time: [a_timing_nanos](#a_timing_nanos)
<a id="r_capture_method_entry_timestamp_conditional"></a>
### r_capture_method_entry_timestamp_conditional
Conditionally captures the entry timestamp of the current method into method_entry_time
The capturing will only happen it capture_time_condition is defined as true.
For example, http instrumentation define capture_time_condition based on http_is_entry
The condition is there to prevent unnecessary invocations of System.nanoTime(), which can be expensive

#### Attributes:

##### entry
- method_entry_time: [a_timing_nanos](#a_timing_nanos)
<a id="r_http_capture_method_duration"></a>
### r_http_capture_method_duration
Captures the duration of the method as http_duration.
Capturing only takes palce if http_is_entry is true for the method!

#### Include:

- [r_capture_method_duration_conditional](#r_capture_method_duration_conditional)
#### Attributes:

##### entry
- capture_time_condition: [a_assign_true](#a_assign_true)
- method_entry_time: [a_timing_nanos](#a_timing_nanos)
##### exit
- http_duration: [a_assign_value](#a_assign_value)
- method_duration: [a_timing_elapsedMillis](#a_timing_elapsedMillis)
<a id="r_http_client_record_metric"></a>
### r_http_client_record_metric
Records the value provided via http_duration as http/out metric value.
It is expected that the http data (http_path, http_status, etc) has been populated.

#### Metrics:

##### [http/out/responsetime]

###### Value: http_duration

###### Data Tags: 

- http_host: http_target_host
- http_path: http_path
- http_status: http_status
- http_method: http_method
- error: http_is_error
<a id="r_http_client_record_metric_on_method"></a>
### r_http_client_record_metric_on_method
Records the duration of the method on which this rule is applied as http/out metric value.
Recording only takes place if http_is_entry is true.
It is expected that the http data (http_path, http_status, etc) has been populated in this case.

#### Include:

- [r_http_capture_method_duration](#r_http_capture_method_duration)
- [r_http_client_record_metric](#r_http_client_record_metric)
#### Attributes:

##### entry
- capture_time_condition: [a_assign_true](#a_assign_true)
- method_entry_time: [a_timing_nanos](#a_timing_nanos)
##### exit
- http_duration: [a_assign_value](#a_assign_value)
- method_duration: [a_timing_elapsedMillis](#a_timing_elapsedMillis)
<a id="r_http_client_tracing_default_attributes"></a>
### r_http_client_tracing_default_attributes
This rule is included by all HTTP Client tracing rules to define which attributes should be added to the span.

#### Include:

- [r_http_tracing_default_attributes](#r_http_tracing_default_attributes)
#### Attributes:

##### entry
- method_fqn: [a_method_getFQN](#a_method_getFQN)
#### Tracing:

##### start-span: null

##### Attributes:

- http.host: http_target_host
<a id="r_http_parametrize_path"></a>
### r_http_parametrize_path
This rule is included by all HTTP instrumentation to derive the parametrized http_path from http_raw_path.
It should therefore be extended via custom includes in order to perform application specific parametrizations.

#### Include:

- [r_http_parametrize_path_remove_ids](#r_http_parametrize_path_remove_ids)
#### Attributes:

##### entry
- http_path: [a_assign_value](#a_assign_value)
<a id="r_http_parametrize_path_remove_ids"></a>
### r_http_parametrize_path_remove_ids
The default parametrization inspectIT provides, which removes numeric ID path segments and UUIDs.

#### Attributes:

##### entry
- http_path: [a_regex_replaceAll_multi](#a_regex_replaceAll_multi)
<a id="r_http_server_record_metric"></a>
### r_http_server_record_metric
Records the value provided via http_duration as http/in metric value.
It is expected that the http data (http_path, http_status, etc) has been populated.

#### Metrics:

##### [http/in/responsetime]

###### Value: http_duration

###### Data Tags: 

- http_path: http_path
- http_status: http_status
- http_method: http_method
- error: http_is_error
<a id="r_http_server_record_metric_on_method"></a>
### r_http_server_record_metric_on_method
Records the duration of the method on which this rule is applied as http/in metric value.
Recording only takes place if http_is_entry is true.
It is expected that the http data (http_path, http_status, etc) has been populated in this case.

#### Include:

- [r_http_capture_method_duration](#r_http_capture_method_duration)
- [r_http_server_record_metric](#r_http_server_record_metric)
#### Attributes:

##### entry
- capture_time_condition: [a_assign_true](#a_assign_true)
- method_entry_time: [a_timing_nanos](#a_timing_nanos)
##### exit
- http_duration: [a_assign_value](#a_assign_value)
- method_duration: [a_timing_elapsedMillis](#a_timing_elapsedMillis)
<a id="r_http_server_tracing_default_attributes"></a>
### r_http_server_tracing_default_attributes
This rule is included by all HTTP Server tracing rules to define which attributes should be added to the span.

#### Include:

- [r_http_tracing_default_attributes](#r_http_tracing_default_attributes)
#### Attributes:

##### entry
- method_fqn: [a_method_getFQN](#a_method_getFQN)
<a id="r_http_tracing_default_attributes"></a>
### r_http_tracing_default_attributes
This rule is included by all HTTP Client AND Server tracing rules to define which attributes should be added to the span.

#### Include:

- [r_tracing_global_attributes](#r_tracing_global_attributes)
#### Attributes:

##### entry
- method_fqn: [a_method_getFQN](#a_method_getFQN)
#### Tracing:

##### start-span: null

##### Attributes:

- http.status_code: http_status
- http.method: http_method
- http.path: http_path
<a id="r_http_tracing_span_name_default"></a>
### r_http_tracing_span_name_default
This rule is included by all HTTP tracing rules in order to define the span name.

#### Attributes:

##### entry
- http_span_name: [a_string_concat_3](#a_string_concat_3)
#### Tracing:

##### start-span: null

<a id="r_httpurlconnection_detect_end"></a>
### r_httpurlconnection_detect_end
Tests and sets an "end" marker on this HTTP Url Connection object.
The end marker will only be set exactly once.
This functionality is usually used to distingiush between the first and all other getInputStream() calls.

#### Include:

- [r_httpurlconnection_detect_entry](#r_httpurlconnection_detect_entry)
#### Attributes:

##### entry
- httpurlconnection_is_end: [a_assign_value](#a_assign_value)
- httpurlconnection_prev_end_marker: [a_attachment_put](#a_attachment_put)
##### preEntry
- http_is_entry: [a_entrypoint_check](#a_entrypoint_check)
<a id="r_httpurlconnection_detect_entry"></a>
### r_httpurlconnection_detect_entry
Can be included to detect and exclude nested calls.
For example, an HTTPSurlConnection literally uses a normal HTTPUrlConnection.

#### Attributes:

##### preEntry
- http_is_entry: [a_entrypoint_check](#a_entrypoint_check)
<a id="r_httpurlconnection_detect_start"></a>
### r_httpurlconnection_detect_start
Tests and sets an "start" marker on this HTTP Url Connection object.
The end marker will only be set exactly once.

#### Include:

- [r_httpurlconnection_detect_entry](#r_httpurlconnection_detect_entry)
#### Attributes:

##### entry
- httpurlconnection_is_start: [a_assign_value](#a_assign_value)
- httpurlconnection_prev_start_marker: [a_attachment_put](#a_attachment_put)
##### preEntry
- http_is_entry: [a_entrypoint_check](#a_entrypoint_check)
<a id="r_httpurlconnection_down_propagation"></a>
### r_httpurlconnection_down_propagation
Writes down-propagated data to the HTTP Headers.

#### Include:

- [r_httpurlconnection_detect_entry](#r_httpurlconnection_detect_entry)
#### Attributes:

##### postEntry
- do_down_propagation: [a_httpurlconnection_downPropagation](#a_httpurlconnection_downPropagation)
##### preEntry
- http_is_entry: [a_entrypoint_check](#a_entrypoint_check)
<a id="r_httpurlconnection_extract_details"></a>
### r_httpurlconnection_extract_details
Extracts all http information such as http_status, http_path, etc.

#### Include:

- [r_httpurlconnection_extract_request_details](#r_httpurlconnection_extract_request_details)
- [r_httpurlconnection_extract_response_details](#r_httpurlconnection_extract_response_details)
#### Attributes:

##### entry
- http_method: [a_httpurlconnection_getMethod](#a_httpurlconnection_getMethod)
- http_path: [a_assign_value](#a_assign_value)
- http_raw_path: [a_httpurlconnection_getPath](#a_httpurlconnection_getPath)
- http_target_host: [a_httpurlconnection_getTargetHost](#a_httpurlconnection_getTargetHost)
##### exit
- http_is_error: [a_http_isErrorStatus](#a_http_isErrorStatus)
- http_status: [a_httpurlconnection_getStatus](#a_httpurlconnection_getStatus)
##### preEntry
- http_is_entry: [a_entrypoint_check](#a_entrypoint_check)
<a id="r_httpurlconnection_extract_request_details"></a>
### r_httpurlconnection_extract_request_details
Extracts all http request information such as http_path, http_raw_path, http_method etc.

#### Include:

- [r_httpurlconnection_detect_entry](#r_httpurlconnection_detect_entry)
- [r_http_parametrize_path](#r_http_parametrize_path)
#### Attributes:

##### entry
- http_method: [a_httpurlconnection_getMethod](#a_httpurlconnection_getMethod)
- http_path: [a_assign_value](#a_assign_value)
- http_raw_path: [a_httpurlconnection_getPath](#a_httpurlconnection_getPath)
- http_target_host: [a_httpurlconnection_getTargetHost](#a_httpurlconnection_getTargetHost)
##### preEntry
- http_is_entry: [a_entrypoint_check](#a_entrypoint_check)
<a id="r_httpurlconnection_extract_response_details"></a>
### r_httpurlconnection_extract_response_details
Extracts all http response information such as http_status, http_is_error.

#### Include:

- [r_httpurlconnection_detect_entry](#r_httpurlconnection_detect_entry)
#### Attributes:

##### exit
- http_is_error: [a_http_isErrorStatus](#a_http_isErrorStatus)
- http_status: [a_httpurlconnection_getStatus](#a_httpurlconnection_getStatus)
##### preEntry
- http_is_entry: [a_entrypoint_check](#a_entrypoint_check)
<a id="r_httpurlconnection_http_metric"></a>
### r_httpurlconnection_http_metric
Records HTTP calls done via HTTPUrlConnection via the http/out metric.

#### Include:

- [r_httpurlconnection_extract_details](#r_httpurlconnection_extract_details)
- [r_httpurlconnection_detect_end](#r_httpurlconnection_detect_end)
- [r_http_client_record_metric](#r_http_client_record_metric)
- [r_capture_method_duration_conditional](#r_capture_method_duration_conditional)
#### Attributes:

##### entry
- capture_time_condition: [a_assign_true](#a_assign_true)
- http_method: [a_httpurlconnection_getMethod](#a_httpurlconnection_getMethod)
- http_path: [a_assign_value](#a_assign_value)
- http_raw_path: [a_httpurlconnection_getPath](#a_httpurlconnection_getPath)
- http_target_host: [a_httpurlconnection_getTargetHost](#a_httpurlconnection_getTargetHost)
- httpurlconnection_is_end: [a_assign_value](#a_assign_value)
- httpurlconnection_prev_end_marker: [a_attachment_put](#a_attachment_put)
- method_entry_time: [a_timing_nanos](#a_timing_nanos)
##### exit
- http_duration: [a_assign_value](#a_assign_value)
- http_is_error: [a_http_isErrorStatus](#a_http_isErrorStatus)
- http_status: [a_httpurlconnection_getStatus](#a_httpurlconnection_getStatus)
- method_duration: [a_timing_elapsedMillis](#a_timing_elapsedMillis)
##### preEntry
- http_is_entry: [a_entrypoint_check](#a_entrypoint_check)
<a id="r_httpurlconnection_servicegraph_propagation"></a>
### r_httpurlconnection_servicegraph_propagation
Reads and writes the service graph correlation information on the context (e.g. origin_service and target_service).

#### Include:

- [r_servicegraph_prepare_down_propagation](#r_servicegraph_prepare_down_propagation)
#### Attributes:

##### entry
- servicegraph_origin_service: [a_assign_value](#a_assign_value)
<a id="r_httpurlconnection_servicegraph_record"></a>
### r_httpurlconnection_servicegraph_record
Records an HTTP call done via HTTPUrlConnection as service-call.

#### Include:

- [r_servicegraph_outbound_record_method](#r_servicegraph_outbound_record_method)
- [r_httpurlconnection_detect_end](#r_httpurlconnection_detect_end)
- [r_httpurlconnection_extract_request_details](#r_httpurlconnection_extract_request_details)
#### Attributes:

##### entry
- capture_time_condition: [a_assign_true](#a_assign_true)
- http_method: [a_httpurlconnection_getMethod](#a_httpurlconnection_getMethod)
- http_path: [a_assign_value](#a_assign_value)
- http_raw_path: [a_httpurlconnection_getPath](#a_httpurlconnection_getPath)
- http_target_host: [a_httpurlconnection_getTargetHost](#a_httpurlconnection_getTargetHost)
- httpurlconnection_is_end: [a_assign_value](#a_assign_value)
- httpurlconnection_prev_end_marker: [a_attachment_put](#a_attachment_put)
- method_entry_time: [a_timing_nanos](#a_timing_nanos)
- servicegraph_is_entry: [a_entrypoint_check](#a_entrypoint_check)
- servicegraph_protocol: [a_assign_value](#a_assign_value)
##### exit
- method_duration: [a_timing_elapsedMillis](#a_timing_elapsedMillis)
- servicegraph_duration: [a_assign_value](#a_assign_value)
- servicegraph_is_error: [a_assign_value](#a_assign_value)
- servicegraph_target_external: [a_assign_value](#a_assign_value)
##### postExit
- servicegraph_target_service: [a_assign_null](#a_assign_null)
##### preEntry
- http_is_entry: [a_entrypoint_check](#a_entrypoint_check)
<a id="r_httpurlconnection_tracing_end"></a>
### r_httpurlconnection_tracing_end
Continues an already started span for this HTTPUrlConnection via r_httpurlconnection_tracing_start.
If no span was started yet, a fresh one is started.

#### Include:

- [r_http_tracing_span_name_default](#r_http_tracing_span_name_default)
- [r_http_client_tracing_default_attributes](#r_http_client_tracing_default_attributes)
- [r_httpurlconnection_detect_end](#r_httpurlconnection_detect_end)
- [r_httpurlconnection_extract_request_details](#r_httpurlconnection_extract_request_details)
- [r_httpurlconnection_extract_response_details](#r_httpurlconnection_extract_response_details)
#### Attributes:

##### entry
- http_method: [a_httpurlconnection_getMethod](#a_httpurlconnection_getMethod)
- http_path: [a_assign_value](#a_assign_value)
- http_raw_path: [a_httpurlconnection_getPath](#a_httpurlconnection_getPath)
- http_span_name: [a_string_concat_3](#a_string_concat_3)
- http_target_host: [a_httpurlconnection_getTargetHost](#a_httpurlconnection_getTargetHost)
- httpurlconnection_is_end: [a_assign_value](#a_assign_value)
- httpurlconnection_prev_end_marker: [a_attachment_put](#a_attachment_put)
- httpurlconnection_span: [a_attachment_get](#a_attachment_get)
- method_fqn: [a_method_getFQN](#a_method_getFQN)
##### exit
- http_is_error: [a_http_isErrorStatus](#a_http_isErrorStatus)
- http_status: [a_httpurlconnection_getStatus](#a_httpurlconnection_getStatus)
##### preEntry
- http_is_entry: [a_entrypoint_check](#a_entrypoint_check)
#### Tracing:

##### start-span: true

<a id="r_httpurlconnection_tracing_start"></a>
### r_httpurlconnection_tracing_start
Starts a span for an HTTPUrlConnection before down propagation happens.

#### Include:

- [r_httpurlconnection_detect_start](#r_httpurlconnection_detect_start)
- [r_httpurlconnection_extract_request_details](#r_httpurlconnection_extract_request_details)
- [r_http_tracing_span_name_default](#r_http_tracing_span_name_default)
#### Attributes:

##### entry
- http_method: [a_httpurlconnection_getMethod](#a_httpurlconnection_getMethod)
- http_path: [a_assign_value](#a_assign_value)
- http_raw_path: [a_httpurlconnection_getPath](#a_httpurlconnection_getPath)
- http_span_name: [a_string_concat_3](#a_string_concat_3)
- http_target_host: [a_httpurlconnection_getTargetHost](#a_httpurlconnection_getTargetHost)
- httpurlconnection_is_start: [a_assign_value](#a_assign_value)
- httpurlconnection_prev_start_marker: [a_attachment_put](#a_attachment_put)
##### postEntry
- span_obj: [a_attachment_put](#a_attachment_put)
##### preEntry
- http_is_entry: [a_entrypoint_check](#a_entrypoint_check)
#### Tracing:

##### start-span: true

<a id="r_httpurlconnection_up_propagation"></a>
### r_httpurlconnection_up_propagation
Reads up-propagated data from the response HTTP Headers.

#### Include:

- [r_httpurlconnection_detect_entry](#r_httpurlconnection_detect_entry)
#### Attributes:

##### preExit
- do_up_propagation: [a_httpurlconnection_upPropagation](#a_httpurlconnection_upPropagation)
##### preEntry
- http_is_entry: [a_entrypoint_check](#a_entrypoint_check)
<a id="r_jdbc_detect_entry"></a>
### r_jdbc_detect_entry
Can be included to detect the entry into the first JDBC layer.

#### Attributes:

##### entry
- jdbc_is_entry: [a_entrypoint_check](#a_entrypoint_check)
<a id="r_jdbc_extract_url"></a>
### r_jdbc_extract_url
Can be included to extract the url of the JDBC url.
Extraction only happens for the first JDBC layer, which might be a JDBC proxy.

#### Include:

- [r_jdbc_detect_entry](#r_jdbc_detect_entry)
#### Attributes:

##### entry
- jdbc_is_entry: [a_entrypoint_check](#a_entrypoint_check)
- jdbc_url: [a_jdbc_getUrl](#a_jdbc_getUrl)
<a id="r_jdbc_prepared_sql_stop_propagation"></a>
### r_jdbc_prepared_sql_stop_propagation
This rule does not provide any functionality but a performance benefit.
Driver-specific instrumentations can write 'prepared_sql' which gets up-propagated.
This rule limits the up-propagation to the JDBC scope, because propagating it up further is not necessary.

#### Include:

- [r_jdbc_detect_entry](#r_jdbc_detect_entry)
#### Attributes:

##### entry
- jdbc_is_entry: [a_entrypoint_check](#a_entrypoint_check)
##### postExit
- prepared_sql: [a_assign_null](#a_assign_null)
<a id="r_jdbc_servicegraph_record"></a>
### r_jdbc_servicegraph_record
Records JDBC statement executions in the service graph.

#### Include:

- [r_servicegraph_outbound_record_method](#r_servicegraph_outbound_record_method)
- [r_jdbc_detect_entry](#r_jdbc_detect_entry)
- [r_jdbc_extract_url](#r_jdbc_extract_url)
#### Attributes:

##### entry
- capture_time_condition: [a_assign_true](#a_assign_true)
- jdbc_is_entry: [a_entrypoint_check](#a_entrypoint_check)
- jdbc_url: [a_jdbc_getUrl](#a_jdbc_getUrl)
- method_entry_time: [a_timing_nanos](#a_timing_nanos)
- servicegraph_is_entry: [a_entrypoint_check](#a_entrypoint_check)
- servicegraph_protocol: [a_assign_value](#a_assign_value)
- servicegraph_target_external: [a_assign_value](#a_assign_value)
##### exit
- method_duration: [a_timing_elapsedMillis](#a_timing_elapsedMillis)
- servicegraph_duration: [a_assign_value](#a_assign_value)
- servicegraph_is_error: [a_logic_isNotNull](#a_logic_isNotNull)
##### postExit
- servicegraph_target_service: [a_assign_null](#a_assign_null)
<a id="r_jdbc_tracing_defaults"></a>
### r_jdbc_tracing_defaults


#### Include:

- [r_jdbc_detect_entry](#r_jdbc_detect_entry)
- [r_jdbc_extract_url](#r_jdbc_extract_url)
- [r_tracing_global_attributes](#r_tracing_global_attributes)
#### Attributes:

##### entry
- db_type_sql: [a_assign_value](#a_assign_value)
- jdbc_is_entry: [a_entrypoint_check](#a_entrypoint_check)
- jdbc_url: [a_jdbc_getUrl](#a_jdbc_getUrl)
- method_fqn: [a_method_getFQN](#a_method_getFQN)
#### Tracing:

##### start-span: true

##### Attributes:

- db.type: db_type_sql
- db.url: jdbc_url
<a id="r_jdbc_tracing_preparedstatement"></a>
### r_jdbc_tracing_preparedstatement
Traces execute/executeUpdate/executeBatch calls with prepared SQL.

#### Include:

- [r_jdbc_tracing_defaults](#r_jdbc_tracing_defaults)
#### Attributes:

##### entry
- db_type_sql: [a_assign_value](#a_assign_value)
- jdbc_is_entry: [a_entrypoint_check](#a_entrypoint_check)
- jdbc_url: [a_jdbc_getUrl](#a_jdbc_getUrl)
- method_fqn: [a_method_getFQN](#a_method_getFQN)
#### Tracing:

##### start-span: null

##### Attributes:

- db.statement: prepared_sql
<a id="r_jdbc_tracing_statement_execute"></a>
### r_jdbc_tracing_statement_execute
Traces execute/executeUpdate calls with non-prepared SQL (the SQL is given as method argument)

#### Include:

- [r_jdbc_tracing_defaults](#r_jdbc_tracing_defaults)
#### Attributes:

##### entry
- db_type_sql: [a_assign_value](#a_assign_value)
- jdbc_is_entry: [a_entrypoint_check](#a_entrypoint_check)
- jdbc_url: [a_jdbc_getUrl](#a_jdbc_getUrl)
- method_fqn: [a_method_getFQN](#a_method_getFQN)
#### Tracing:

##### start-span: null

##### Attributes:

- db.statement: _arg0
<a id="r_jdbc_tracing_statement_executeBatch"></a>
### r_jdbc_tracing_statement_executeBatch
Traces executeBatch calls with non-prepared SQL.

#### Include:

- [r_jdbc_tracing_defaults](#r_jdbc_tracing_defaults)
#### Attributes:

##### entry
- db_type_sql: [a_assign_value](#a_assign_value)
- jdbc_is_entry: [a_entrypoint_check](#a_entrypoint_check)
- jdbc_url: [a_jdbc_getUrl](#a_jdbc_getUrl)
- method_fqn: [a_method_getFQN](#a_method_getFQN)
<a id="r_method_metric"></a>
### r_method_metric
This rule captures the method's duration and invocation count as a method/duration metric. 
The metric is tagged with the signature of the method as well as the name of the declaring class.
In addition an error tag is set depending on whether the method threw an exception or not.

The rule can either be included or scopes can be added directly to it.

#### Include:

- [r_capture_method_duration](#r_capture_method_duration)
#### Attributes:

##### exit
- class_name: [a_method_getClassFQN](#a_method_getClassFQN)
- method_duration: [a_timing_elapsedMillis](#a_timing_elapsedMillis)
- method_is_error: [a_logic_isNotNull](#a_logic_isNotNull)
- method_name_with_params: [a_method_getNameWithParameters](#a_method_getNameWithParameters)
##### entry
- capture_time_condition: [a_assign_true](#a_assign_true)
- method_entry_time: [a_timing_nanos](#a_timing_nanos)
#### Metrics:

##### [method/duration]

###### Value: method_duration

###### Data Tags: 

- class: class_name
- method: method_name_with_params
- error: method_is_error
<a id="r_servicegraph_capture_method_duration"></a>
### r_servicegraph_capture_method_duration
Utility rule used for both inbound & outbout service graph traffic.
Captures the duration of the current method as servicegraph_duration
This capturing takes only place if servicegraph_is_entry is populated with "true"

#### Include:

- [r_capture_method_duration_conditional](#r_capture_method_duration_conditional)
#### Attributes:

##### entry
- capture_time_condition: [a_assign_true](#a_assign_true)
- method_entry_time: [a_timing_nanos](#a_timing_nanos)
##### exit
- method_duration: [a_timing_elapsedMillis](#a_timing_elapsedMillis)
- servicegraph_duration: [a_assign_value](#a_assign_value)
<a id="r_servicegraph_inbound_record_method"></a>
### r_servicegraph_inbound_record_method
Can be included to record the current method invocation as an inbound service graph call.
The includer should populate servicegraph_is_entry if this method is the correct entry point.
In addition, servicegraph_origin_external can be populated if an external origin is present.

#### Include:

- [r_servicegraph_capture_method_duration](#r_servicegraph_capture_method_duration)
- [r_servicegraph_inbound_record_metric](#r_servicegraph_inbound_record_metric)
#### Attributes:

##### entry
- capture_time_condition: [a_assign_true](#a_assign_true)
- method_entry_time: [a_timing_nanos](#a_timing_nanos)
- servicegraph_origin_service: [a_assign_null](#a_assign_null)
- servicegraph_origin_service_local: [a_assign_value](#a_assign_value)
##### exit
- method_duration: [a_timing_elapsedMillis](#a_timing_elapsedMillis)
- servicegraph_duration: [a_assign_value](#a_assign_value)
<a id="r_servicegraph_inbound_record_metric"></a>
### r_servicegraph_inbound_record_metric
Records inbound traffic as service graph call.
When this rule is included, it is expected that servicegraph_duration, servicegraph_protocol and servicegraph_is_error are populated.
If there was an external origin, servicegraph_origin_external should contain it's name.
In addition this rule prevent invalid further down-propagation of servicegraph_origin_service by moving it into the JVM_LOCAL variable servicegraph_origin_service_local

#### Attributes:

##### entry
- servicegraph_origin_service: [a_assign_null](#a_assign_null)
- servicegraph_origin_service_local: [a_assign_value](#a_assign_value)
#### Metrics:

##### [service/in/responsetime]

###### Value: servicegraph_duration

###### Data Tags: 

- origin_service: servicegraph_origin_service_local
- origin_external: servicegraph_origin_external
- protocol: servicegraph_protocol
- error: servicegraph_is_error
<a id="r_servicegraph_outbound_record_method"></a>
### r_servicegraph_outbound_record_method
Can be included to record the current method invocation as an outbound service graph call.
The includer should populate servicegraph_is_entry if this method is the correct entry point.
In addition, servicegraph_target_external can be populated if an external target is present.

#### Include:

- [r_servicegraph_capture_method_duration](#r_servicegraph_capture_method_duration)
- [r_servicegraph_outbound_record_metric](#r_servicegraph_outbound_record_metric)
#### Attributes:

##### entry
- capture_time_condition: [a_assign_true](#a_assign_true)
- method_entry_time: [a_timing_nanos](#a_timing_nanos)
##### exit
- method_duration: [a_timing_elapsedMillis](#a_timing_elapsedMillis)
- servicegraph_duration: [a_assign_value](#a_assign_value)
##### postExit
- servicegraph_target_service: [a_assign_null](#a_assign_null)
<a id="r_servicegraph_outbound_record_metric"></a>
### r_servicegraph_outbound_record_metric
Records outbound traffic as service graph call
When this rule is included, it is expected that servicegraph_duration, servicegraph_protocol and servicegraph_is_error are populated.
If there was an external target, servicegraph_target_external should contain it's name.
In addition further invalid up-propagation of the target service is prevented.

#### Attributes:

##### postExit
- servicegraph_target_service: [a_assign_null](#a_assign_null)
#### Metrics:

##### [service/out/responsetime]

###### Value: servicegraph_duration

###### Data Tags: 

- target_service: servicegraph_target_service
- target_external: servicegraph_target_external
- protocol: servicegraph_protocol
- error: servicegraph_is_error
<a id="r_servicegraph_prepare_down_propagation"></a>
### r_servicegraph_prepare_down_propagation
This rule prepares down-propagation to include service-graph related information
Namely this ensures that the current service name is included as origin service

#### Attributes:

##### entry
- servicegraph_origin_service: [a_assign_value](#a_assign_value)
<a id="r_servicegraph_prepare_up_propagation"></a>
### r_servicegraph_prepare_up_propagation
This rule prepares up-propagation to include service-graph related information.
Namely this ensures that the current service name is included as target service.
Because depending on the used communication up-propagation can happen on both exit or entry, this rule configures servicegraph_target_service in both entry and exit of the method.

#### Attributes:

##### entry
- servicegraph_target_service: [a_assign_value](#a_assign_value)
##### exit
- servicegraph_target_service: [a_assign_value](#a_assign_value)
<a id="r_servletapi_detect_entry"></a>
### r_servletapi_detect_entry
Marks the first service() or doFilter() calls as entry for which the ServletRequest is an HTTPServletRequest.

#### Attributes:

##### preEntry
- http_is_entry: [a_entrypoint_check](#a_entrypoint_check)
- is_http_servlet: [a_servletrequest_isHttp](#a_servletrequest_isHttp)
<a id="r_servletapi_downPropagation"></a>
### r_servletapi_downPropagation
Reads down-propagated data from the request HTTP headers.

#### Include:

- [r_servletapi_detect_entry](#r_servletapi_detect_entry)
#### Attributes:

##### preEntry
- do_down_propagation: [a_servletapi_downPropagation](#a_servletapi_downPropagation)
- http_is_entry: [a_entrypoint_check](#a_entrypoint_check)
- is_http_servlet: [a_servletrequest_isHttp](#a_servletrequest_isHttp)
<a id="r_servletapi_extract_details"></a>
### r_servletapi_extract_details
Extracts all http details, such as http_path, http_method, http_status, etc.

#### Include:

- [r_servletapi_detect_entry](#r_servletapi_detect_entry)
- [r_http_parametrize_path](#r_http_parametrize_path)
#### Attributes:

##### entry
- http_method: [a_httpservletrequest_getMethod](#a_httpservletrequest_getMethod)
- http_path: [a_assign_value](#a_assign_value)
- http_raw_path: [a_httpservletrequest_getPath](#a_httpservletrequest_getPath)
##### exit
- http_is_error: [a_http_isErrorStatus](#a_http_isErrorStatus)
- http_status: [a_httpservletresponse_getStatus](#a_httpservletresponse_getStatus)
##### preEntry
- http_is_entry: [a_entrypoint_check](#a_entrypoint_check)
- is_http_servlet: [a_servletrequest_isHttp](#a_servletrequest_isHttp)
<a id="r_servletapi_http_metric"></a>
### r_servletapi_http_metric
Records the http/in metrics for calls received via the servlet API.

#### Include:

- [r_servletapi_extract_details](#r_servletapi_extract_details)
- [r_http_server_record_metric_on_method](#r_http_server_record_metric_on_method)
#### Attributes:

##### entry
- capture_time_condition: [a_assign_true](#a_assign_true)
- http_method: [a_httpservletrequest_getMethod](#a_httpservletrequest_getMethod)
- http_path: [a_assign_value](#a_assign_value)
- http_raw_path: [a_httpservletrequest_getPath](#a_httpservletrequest_getPath)
- method_entry_time: [a_timing_nanos](#a_timing_nanos)
##### exit
- http_duration: [a_assign_value](#a_assign_value)
- http_is_error: [a_http_isErrorStatus](#a_http_isErrorStatus)
- http_status: [a_httpservletresponse_getStatus](#a_httpservletresponse_getStatus)
- method_duration: [a_timing_elapsedMillis](#a_timing_elapsedMillis)
##### preEntry
- http_is_entry: [a_entrypoint_check](#a_entrypoint_check)
- is_http_servlet: [a_servletrequest_isHttp](#a_servletrequest_isHttp)
<a id="r_servletapi_servicegraph_propagation"></a>
### r_servletapi_servicegraph_propagation
Enables the propagation of the "origin_service" for the service graph.

#### Include:

- [r_servicegraph_prepare_up_propagation](#r_servicegraph_prepare_up_propagation)
#### Attributes:

##### entry
- servicegraph_target_service: [a_assign_value](#a_assign_value)
##### exit
- servicegraph_target_service: [a_assign_value](#a_assign_value)
<a id="r_servletapi_servicegraph_record"></a>
### r_servletapi_servicegraph_record
Records inbound HTTP calls via the Servlet API in the service graph.

#### Include:

- [r_servicegraph_inbound_record_method](#r_servicegraph_inbound_record_method)
#### Attributes:

##### entry
- capture_time_condition: [a_assign_true](#a_assign_true)
- method_entry_time: [a_timing_nanos](#a_timing_nanos)
- servicegraph_is_entry: [a_entrypoint_check](#a_entrypoint_check)
- servicegraph_origin_service: [a_assign_null](#a_assign_null)
- servicegraph_origin_service_local: [a_assign_value](#a_assign_value)
- servicegraph_protocol: [a_assign_value](#a_assign_value)
##### exit
- method_duration: [a_timing_elapsedMillis](#a_timing_elapsedMillis)
- servicegraph_duration: [a_assign_value](#a_assign_value)
- servicegraph_is_error: [a_assign_value](#a_assign_value)
<a id="r_servletapi_servlet_filter_upPropagation"></a>
### r_servletapi_servlet_filter_upPropagation
Writes up-propagated data to the response HTTP headers.

#### Attributes:

##### postExit
- do_up_propagation: [a_servletapi_upPropagation](#a_servletapi_upPropagation)
<a id="r_servletapi_servletresponse_upPropagation"></a>
### r_servletapi_servletresponse_upPropagation


#### Attributes:

##### postEntry
- do_up_propagation: [a_servletapi_upPropagation](#a_servletapi_upPropagation)
<a id="r_servletapi_tracing"></a>
### r_servletapi_tracing
Enables tracing of HTTP calls received via the servlet API.

#### Include:

- [r_servletapi_extract_details](#r_servletapi_extract_details)
- [r_http_tracing_span_name_default](#r_http_tracing_span_name_default)
- [r_http_server_tracing_default_attributes](#r_http_server_tracing_default_attributes)
#### Attributes:

##### entry
- http_method: [a_httpservletrequest_getMethod](#a_httpservletrequest_getMethod)
- http_path: [a_assign_value](#a_assign_value)
- http_raw_path: [a_httpservletrequest_getPath](#a_httpservletrequest_getPath)
- http_span_name: [a_string_concat_3](#a_string_concat_3)
- method_fqn: [a_method_getFQN](#a_method_getFQN)
##### exit
- http_is_error: [a_http_isErrorStatus](#a_http_isErrorStatus)
- http_status: [a_httpservletresponse_getStatus](#a_httpservletresponse_getStatus)
##### preEntry
- http_is_entry: [a_entrypoint_check](#a_entrypoint_check)
- is_http_servlet: [a_servletrequest_isHttp](#a_servletrequest_isHttp)
#### Tracing:

##### start-span: true

<a id="r_trace_method"></a>
### r_trace_method
This rule records the current method as a span.
In addition, the FQN of the method is added as a tag and the error status is set based on if the method threw an exception

This rule can be either included or scopes can be directly added to it.

#### Include:

- [r_tracing_global_attributes](#r_tracing_global_attributes)
#### Attributes:

##### entry
- method_fqn: [a_method_getFQN](#a_method_getFQN)
#### Tracing:

##### start-span: true

<a id="r_tracing_exception_attributes"></a>
### r_tracing_exception_attributes
This rule by default only tags the exception name and message (using the toString() method).
If required, it can be enhanced to include the stack trace.

#### Tracing:

##### start-span: null

##### Attributes:

- java.exception: _thrown
<a id="r_tracing_fqn_attribute"></a>
### r_tracing_fqn_attribute
This rule adds the fully qualified name of the method as span attribute

#### Attributes:

##### entry
- method_fqn: [a_method_getFQN](#a_method_getFQN)
#### Tracing:

##### start-span: null

##### Attributes:

- java.fqn: method_fqn
<a id="r_tracing_global_attributes"></a>
### r_tracing_global_attributes
This rule should be included by all other tracing rules.
It allows to specify data which is added to all spans as attributes, e.g. a business transaction

#### Include:

- [r_tracing_exception_attributes](#r_tracing_exception_attributes)
- [r_tracing_fqn_attribute](#r_tracing_fqn_attribute)
#### Attributes:

##### entry
- method_fqn: [a_method_getFQN](#a_method_getFQN)
Actions
-------
<a id="a_apacheclient_downPropagation"></a>
### a_apacheclient_downPropagation
Writes down-propagated context data to the HTTP Headers.

#### Inputs:

- HttpMessage _arg1: 
- InspectitContext _context: 
<a id="a_apacheclient_getMethod"></a>
### a_apacheclient_getMethod
Extracts the HTTP Method of the request, e.g. GET or POST.

#### Inputs:

- HttpRequest _arg1: 
<a id="a_apacheclient_getPath"></a>
### a_apacheclient_getPath
Extracts the target http path.

#### Inputs:

- HttpRequest _arg1: 
<a id="a_apacheclient_getStatus"></a>
### a_apacheclient_getStatus
Extracts the response status in case the request succeeded.
If an exception is thrown instead, it's name is returned (e.g. SocketTimeoutException).

#### Inputs:

- HttpResponse _returnValue: 
- Throwable _thrown: 
<a id="a_apacheclient_toHostString"></a>
### a_apacheclient_toHostString
Extracts the target of the request in the form host:port.

#### Inputs:

- HttpHost _arg0: 
<a id="a_apacheclient_upPropagation"></a>
### a_apacheclient_upPropagation
Reads up-propagated context data from the HTTP Headers.

#### Inputs:

- HttpMessage _returnValue: 
- InspectitContext _context: 
<a id="a_assign_false"></a>
### a_assign_false


<a id="a_assign_null"></a>
### a_assign_null


<a id="a_assign_true"></a>
### a_assign_true


<a id="a_assign_value"></a>
### a_assign_value


#### Inputs:

- Object value: 
<a id="a_attachment_get"></a>
### a_attachment_get
Reads a given attachment from a target object.

#### Inputs:

- ObjectAttachments _attachments: 
- Object target: 
- String key: 
<a id="a_attachment_put"></a>
### a_attachment_put
Replaces a given attachment of a target object with a new one, returns the previous value.

#### Inputs:

- ObjectAttachments _attachments: 
- Object target: 
- String key: 
- Object value: 
<a id="a_attachment_remove"></a>
### a_attachment_remove
Removes a given attachment from a target object, returning the previously attached value.

#### Inputs:

- ObjectAttachments _attachments: 
- Object target: 
- String key: 
<a id="a_debug_println"></a>
### a_debug_println
Prints a given Object to stdout.

#### Inputs:

- Object value: 
<a id="a_debug_println_2"></a>
### a_debug_println_2
Prints two given Objects to stdout.

#### Inputs:

- Object a: 
- Object b: 
<a id="a_entrypoint_check"></a>
### a_entrypoint_check
Utility function to detect entry methods.
This functions checks if a given "marker" is present on the context.
The marker should be configured for down-propagation.
If the marker is not present, it is added to the context and "true" is returned.
If it is present, false is returned.
E.g. if methodA calls methodB and they both invoke "a_entrypoint_check" with the same marker,
"true" will only be returned for methodA.

#### Inputs:

- InspectitContext _context: The current context of inspectIT
- String marker: The marker that should be checked for
<a id="a_http_isErrorStatus"></a>
### a_http_isErrorStatus
Utility action which checks if a provided HTTP status (either as string or number) is an error code
Everything except "null", 2xx and 3xx is interpreted as error.'

#### Inputs:

- Object status: 
<a id="a_httpservletrequest_getMethod"></a>
### a_httpservletrequest_getMethod
Extracts the method path from the HttpServletRequest.

#### Inputs:

- HttpServletRequest request: 
<a id="a_httpservletrequest_getPath"></a>
### a_httpservletrequest_getPath
Extracts the request path from the HttpServletRequest.

#### Inputs:

- HttpServletRequest request: 
<a id="a_httpservletresponse_getStatus"></a>
### a_httpservletresponse_getStatus
Extracts the response status from the given HttpServletResponse.

#### Inputs:

- HttpServletResponse response: 
<a id="a_httpurlconnection_downPropagation"></a>
### a_httpurlconnection_downPropagation
Writes down-propagated data to the HTTP Headers.

#### Inputs:

- HttpURLConnection _this: 
- ObjectAttachments _attachments: 
- InspectitContext _context: 
<a id="a_httpurlconnection_getMethod"></a>
### a_httpurlconnection_getMethod
Extracts the method of the request, e.g. GET or POST.

#### Inputs:

- java.net.HttpURLConnection _this: 
<a id="a_httpurlconnection_getPath"></a>
### a_httpurlconnection_getPath
Extracts the target path of the request.

#### Inputs:

- java.net.HttpURLConnection _this: 
<a id="a_httpurlconnection_getStatus"></a>
### a_httpurlconnection_getStatus
Extracts the response status code if the request returned without an exception.
Otherwise returns the name of the exception, e.g. SocketTimeoutException.'

#### Inputs:

- java.net.HttpURLConnection _this: 
<a id="a_httpurlconnection_getTargetHost"></a>
### a_httpurlconnection_getTargetHost
Extracts the target of the request in the format "host:port".

#### Inputs:

- HttpURLConnection _this: 
<a id="a_httpurlconnection_upPropagation"></a>
### a_httpurlconnection_upPropagation
Reads up-propagated data from the HTTP Headers

#### Inputs:

- HttpURLConnection _this: 
- ObjectAttachments _attachments: 
- InspectitContext _context: 
<a id="a_jdbc_getUrl"></a>
### a_jdbc_getUrl
Extracts the name of the JDBC connection for a given statement.

#### Inputs:

- Statement statement: 
<a id="a_logic_and"></a>
### a_logic_and
Returns true, if both provided arguments are not null and true.

#### Inputs:

- Boolean a: 
- Boolean b: 
<a id="a_logic_isNotNull"></a>
### a_logic_isNotNull
Returns true, if the given Object is not null, false otherwise.

#### Inputs:

- Object value: 
<a id="a_logic_isNull"></a>
### a_logic_isNull
Returns true, if the given Object is null, false otherwise.

#### Inputs:

- Object value: 
<a id="a_logic_isTrueOrNotNull"></a>
### a_logic_isTrueOrNotNull
Returns whether the input value is set or represents a true value. 
The action will return true in case a true-Boolean or any other object is passed into the input parameter.

Since: 1.14.0'

#### Inputs:

- Object value: 
<a id="a_logic_or"></a>
### a_logic_or
Returns true, if one of the provided arguments is not null and true.

#### Inputs:

- Boolean a: 
- Boolean b: 
<a id="a_method_getClassFQN"></a>
### a_method_getClassFQN
Returns the full qualified name of the class declaring the current method, e.g. "my.package.MyClass".

#### Inputs:

- Class _class: 
<a id="a_method_getFQN"></a>
### a_method_getFQN
Returns the full qualified name of the current method, e.g. "my.package.MyClass.myMethod".

#### Inputs:

- Class _class: 
- String _methodName: 
- Class[] _parameterTypes: 
<a id="a_method_getNameWithParameters"></a>
### a_method_getNameWithParameters
Returns the name of the method with the simple parameter list, e.g. "myMethod(int, String, MyClass)".

#### Inputs:

- String _methodName: 
- Class[] _parameterTypes: 
<a id="a_regex_extractFirst"></a>
### a_regex_extractFirst
Checks if the given input string has any match for the provided regex.
If it does, the result is returned. If "result" is a string, then capturing groups ($1,$2, etc) are populated.
If the input string is null or the input string does not match the regex, null is returned.

#### Inputs:

- String string: 
- String pattern: 
- Object result: 
<a id="a_regex_extractFirst_multi"></a>
### a_regex_extractFirst_multi
Checks if the given input string contains a match for any of the provided regexes.
If it does, the result for the matching regex is returned. If "result" is a string, then capturing groups ($1,$2, etc) are populated.
The checking happens in the order of the list, meaning that the replacement for the first matching regex is returned.
If the input string is null or the input string does not match the regex, null is returned. 

the patterns_and_results are expected to be a list of the following form:
constant-input:
  patterns_and_results:
    - pattern: <regexA>
    result: <resultA>
    - pattern: <regexB>
    result: <resultA>

#### Inputs:

- String string: 
- Map patterns_and_results: 
<a id="a_regex_extractMatch"></a>
### a_regex_extractMatch
Checks if the given input string fully matches the provided regex.
If it does, the result is returned. If "result" is a string, then capturing groups ($1,$2, etc) are populated.
If the input string is null or the input string does not match the regex, null is returned.

#### Inputs:

- String string: 
- String pattern: 
- Object result: 
<a id="a_regex_extractMatch_multi"></a>
### a_regex_extractMatch_multi
Checks if the given input string fully matches any of the provided regexes.
If it does, the result for the matching regex is returned. If "result" is a string, then capturing groups ($1,$2, etc) are populated.
The checking happens in the order of the list, meaning that the replacement for the first matching regex is returned.
If the input string is null or the input string does not match the regex, null is returned. 

the patterns_and_results are expected to be a list of the following form:
constant-input:
  patterns_and_results:
    - pattern: <regexA>
      result: <resultA>
    - pattern: <regexB>
      result: <resultA>

#### Inputs:

- String string: 
- Map patterns_and_results: 
<a id="a_regex_replaceAll"></a>
### a_regex_replaceAll
Replaces all matches for the provided regex pattern with the given replacement string.
If the input string is null, null is returned.

#### Inputs:

- String string: 
- String pattern: 
- String replacement: 
<a id="a_regex_replaceAll_multi"></a>
### a_regex_replaceAll_multi
Replaces all matches for all of the provided regex patterns with the given replacement strings.
If the input string is null, null is returned.

the patterns_and_replacements are expected to be a list of the following form:
constant-input:
  patterns_and_replacements:
  - pattern: <regexA>
  replacement: <replacementA>
  - pattern: <regexB>
  replacement: <replacementA>
  
The replacement is executed in the order of the inputs.

#### Inputs:

- String string: 
- Map patterns_and_replacements: 
<a id="a_regex_replaceFirst"></a>
### a_regex_replaceFirst
Replaces the first match for the provided regex pattern with the given replacement string.
If the input string is null, null is returned.

#### Inputs:

- String string: 
- String pattern: 
- String replacement: 
<a id="a_regex_replaceMatch"></a>
### a_regex_replaceMatch
Replaces the provided input string if it fully matches a given regex.
If the input string is null, null is returned.

#### Inputs:

- String string: 
- String pattern: 
- String replacement: 
<a id="a_regex_replaceMatch_multi"></a>
### a_regex_replaceMatch_multi
Scans the provided list of regexes to check if any matches the input string fully.
If a match is found, it is replaced with the provided replacement and returned.
If the input string is null, null is returned.

the patterns_and_replacements are expected to be a list of the following form:
constant-input:
  patterns_and_replacements:
    - pattern: <regexA>
      replacement: <replacementA>
    - pattern: <regexB>
      replacement: <replacementA>

The replacement is executed in the order of the inputs.

#### Inputs:

- String string: 
- Map patterns_and_replacements: 
<a id="a_servletapi_downPropagation"></a>
### a_servletapi_downPropagation
Reads down-propagated data from the request HTTP Headers.

#### Inputs:

- ServletRequest _arg0: 
- InspectitContext _context: 
<a id="a_servletapi_upPropagation"></a>
### a_servletapi_upPropagation
Writes up-propagated data to the response HTTP Headers.

#### Inputs:

- ServletResponse response: 
- InspectitContext _context: 
<a id="a_servletrequest_isHttp"></a>
### a_servletrequest_isHttp
Checks if a given ServletRequest is an instance of HttpServletRequest.

#### Inputs:

- ServletRequest request: 
<a id="a_string_concat"></a>
### a_string_concat
Concatenates two provided inputs to a single string.

#### Inputs:

- Object a: 
- Object b: 
<a id="a_string_concat_3"></a>
### a_string_concat_3
Concatenates three provided inputs to a single string.

#### Inputs:

- Object a: 
- Object b: 
- Object c: 
<a id="a_timing_elapsedMillis"></a>
### a_timing_elapsedMillis
Computes the elapsed milliseconds as double since a given nanosecond-timestamp

#### Inputs:

- long since_nanos: the timestamp captured via System.nanoTime() to compare against
<a id="a_timing_nanos"></a>
### a_timing_nanos
Computes a nanosecond-timestamp as a long for the current point in time.

Metrics
-------
### [disk/free]
free disk space

Unit: bytes
### [disk/total]
total disk space

Unit: bytes
### [http/in/responsetime]


Unit: ms
### [http/out/responsetime]


Unit: ms
### [inspectit/self/action/execution-time]
the execution time of the action

Unit: us
### [inspectit/self/duration]
inspectIT Ocelot self-monitoring duration

Unit: us
### [inspectit/self/instrumentation-queue-size]
the number of pending classes inspectIT has to check if they require instrumentation updates

Unit: classes
### [inspectit/self/instrumented-classes]
the number of classes currently instrumented by inspectIT

Unit: classes
### [inspectit/self/logs]
the number of log events

Unit: log events
### [jvm/buffer/count]
an estimate of the number of buffers in the pool

Unit: buffers
### [jvm/buffer/memory/used]
an estimate of the memory that the JVM is using for this buffer pool

Unit: bytes
### [jvm/buffer/total/capacity]
an estimate of the total capacity of the buffers in this pool

Unit: bytes
### [jvm/classes/loaded]
total number of classes currently loaded in the JVM

Unit: classes
### [jvm/classes/unloaded]
total number of classes which have been unloaded since the start of the JVM

Unit: classes
### [jvm/gc/concurrent/phase/time]
the time spent in concurrent GC phases

Unit: ms
### [jvm/gc/live/data/size]
the size of the old generation memory pool after a full GC

Unit: bytes
### [jvm/gc/max/data/size]
the maximum size of the old generation memory pool

Unit: bytes
### [jvm/gc/memory/allocated]
the increase in the size of the young generation memory pool after one GC to before the next

Unit: bytes
### [jvm/gc/memory/promoted]
the count of positive increases in the size of the old generation memory pool before GC to after GC

Unit: bytes
### [jvm/gc/pause]
the time spent in GC pause

Unit: ms
### [jvm/memory/committed]
the amount of memory that is committed for the JVM to use

Unit: bytes
### [jvm/memory/max]
the maximum amount of memory that can be used for memory management

Unit: bytes
### [jvm/memory/used]
the amount of used memory

Unit: bytes
### [jvm/threads/daemon]
the current number of live daemon threads

Unit: threads
### [jvm/threads/live]
the current number of total live threads

Unit: threads
### [jvm/threads/peak]
the peak number of live threads since the JVM start

Unit: threads
### [jvm/threads/states]
the number of live threads for each state

Unit: threads
### [method/duration]
the duration from method entry to method exit

Unit: ms
### [process/cpu/usage]
the recent cpu usage for the JVM's process

Unit: cores
### [service/in/responsetime]


Unit: ms
### [service/out/responsetime]


Unit: ms
### [system/cpu/count]
the number of processors available to the JVM

Unit: cores
### [system/cpu/usage]
the recent cpu usage for the whole system

Unit: cores
### [system/load/average/1m]
the sum of the number of runnable entities queued to available processors and the number of runnable entities running on the available processors averaged over a period of time

Unit: percentage

