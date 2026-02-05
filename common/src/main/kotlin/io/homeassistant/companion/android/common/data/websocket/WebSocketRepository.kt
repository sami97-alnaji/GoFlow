package com.goflow.app.common.data.websocket

import com.goflow.app.common.data.integration.impl.entities.EntityResponse
import com.goflow.app.common.data.servers.ServerManager
import com.goflow.app.common.data.websocket.impl.WebSocketRepositoryImpl
import com.goflow.app.common.data.websocket.impl.entities.AreaRegistryResponse
import com.goflow.app.common.data.websocket.impl.entities.AreaRegistryUpdatedEvent
import com.goflow.app.common.data.websocket.impl.entities.AssistPipelineEvent
import com.goflow.app.common.data.websocket.impl.entities.AssistPipelineListResponse
import com.goflow.app.common.data.websocket.impl.entities.AssistPipelineResponse
import com.goflow.app.common.data.websocket.impl.entities.CompressedStateChangedEvent
import com.goflow.app.common.data.websocket.impl.entities.ConversationResponse
import com.goflow.app.common.data.websocket.impl.entities.CurrentUserResponse
import com.goflow.app.common.data.websocket.impl.entities.DeviceRegistryResponse
import com.goflow.app.common.data.websocket.impl.entities.DeviceRegistryUpdatedEvent
import com.goflow.app.common.data.websocket.impl.entities.DomainResponse
import com.goflow.app.common.data.websocket.impl.entities.EntityRegistryResponse
import com.goflow.app.common.data.websocket.impl.entities.EntityRegistryUpdatedEvent
import com.goflow.app.common.data.websocket.impl.entities.GetConfigResponse
import com.goflow.app.common.data.websocket.impl.entities.GetTodosResponse
import com.goflow.app.common.data.websocket.impl.entities.MatterCommissionResponse
import com.goflow.app.common.data.websocket.impl.entities.StateChangedEvent
import com.goflow.app.common.data.websocket.impl.entities.TemplateUpdatedEvent
import com.goflow.app.common.data.websocket.impl.entities.ThreadDatasetResponse
import com.goflow.app.common.data.websocket.impl.entities.ThreadDatasetTlvResponse
import com.goflow.app.common.data.websocket.impl.entities.TriggerEvent
import javax.inject.Inject
import javax.inject.Provider
import kotlinx.coroutines.flow.Flow

interface WebSocketRepository {
    fun getConnectionState(): WebSocketState
    fun shutdown()
    suspend fun sendPing(): Boolean
    suspend fun getCurrentUser(): CurrentUserResponse?
    suspend fun getConfig(): GetConfigResponse?
    suspend fun getStates(): List<EntityResponse>?
    suspend fun getAreaRegistry(): List<AreaRegistryResponse>?
    suspend fun getDeviceRegistry(): List<DeviceRegistryResponse>?
    suspend fun getEntityRegistry(): List<EntityRegistryResponse>?
    suspend fun getEntityRegistryFor(entityId: String): EntityRegistryResponse?
    suspend fun getServices(): List<DomainResponse>?
    suspend fun getStateChanges(): Flow<StateChangedEvent>?
    suspend fun getStateChanges(entityIds: List<String>): Flow<TriggerEvent>?
    suspend fun getCompressedStateAndChanges(): Flow<CompressedStateChangedEvent>?
    suspend fun getCompressedStateAndChanges(entityIds: List<String>): Flow<CompressedStateChangedEvent>?
    suspend fun getAreaRegistryUpdates(): Flow<AreaRegistryUpdatedEvent>?
    suspend fun getDeviceRegistryUpdates(): Flow<DeviceRegistryUpdatedEvent>?
    suspend fun getEntityRegistryUpdates(): Flow<EntityRegistryUpdatedEvent>?
    suspend fun getTemplateUpdates(template: String): Flow<TemplateUpdatedEvent>?
    suspend fun getNotifications(): Flow<Map<String, Any>>?
    suspend fun ackNotification(confirmId: String): Boolean

    suspend fun getTodos(entityId: String): GetTodosResponse?
    suspend fun updateTodo(entityId: String, todoItem: String, newName: String?, status: String?): Boolean

    /**
     * Request the server to add a Matter device to the network and commission it.
     * @return [MatterCommissionResponse] detailing the server's response, or `null` if the server
     * did not return a response.
     */
    suspend fun commissionMatterDevice(code: String): MatterCommissionResponse?

    /**
     * Request the server to commission a Matter device that is already on the network.
     * @return [MatterCommissionResponse] detailing the server's response, or `null` if the server
     * did not return a response.
     */
    suspend fun commissionMatterDeviceOnNetwork(pin: Long, ip: String): MatterCommissionResponse?

    /**
     * Return a list of all Thread datasets known to the server.
     * @return List with [ThreadDatasetResponse]s, or `null` if not an admin or no response.
     */
    suspend fun getThreadDatasets(): List<ThreadDatasetResponse>?

    /**
     * Return the TLV value for a dataset.
     * @return [ThreadDatasetTlvResponse] for the Thread dataset, or `null` if not found, not an
     * admin or no response.
     */
    suspend fun getThreadDatasetTlv(datasetId: String): ThreadDatasetTlvResponse?

    /**
     * Add a new set of Thread network credentials to the server.
     * @return `true` if the server indicated success
     */
    suspend fun addThreadDataset(tlv: ByteArray): Boolean

    /**
     * Get an Assist response for the given text input. For core >= 2023.5, use [runAssistPipelineForText]
     * instead.
     */
    suspend fun getConversation(speech: String): ConversationResponse?

    /**
     * Get information about an Assist pipeline.
     * @param pipelineId the ID of the pipeline to get details for, if not specified the preferred
     * pipeline will be returned
     * @return [AssistPipelineResponse] detailing the Assist pipeline, or `null` if not found or no
     * response.
     */
    suspend fun getAssistPipeline(pipelineId: String? = null): AssistPipelineResponse?

    /**
     * @return [AssistPipelineListResponse] listing all Assist pipelines and which one is preferred.
     */
    suspend fun getAssistPipelines(): AssistPipelineListResponse?

    /**
     * Run the Assist pipeline for the given text input
     * @return a Flow that will emit all events for the pipeline
     */
    suspend fun runAssistPipelineForText(
        text: String,
        pipelineId: String? = null,
        conversationId: String? = null,
    ): Flow<AssistPipelineEvent>?

    /**
     * Run the Assist pipeline for voice input
     * @return a Flow that will emit all events for the pipeline
     */
    suspend fun runAssistPipelineForVoice(
        sampleRate: Int,
        outputTts: Boolean,
        pipelineId: String? = null,
        conversationId: String? = null,
    ): Flow<AssistPipelineEvent>?

    /**
     * Send voice data for an active Assist pipeline
     * @return `true`/`false` indicating if it was enqueued, or `null` on unexpected failures
     */
    suspend fun sendVoiceData(binaryHandlerId: Int, data: ByteArray): Boolean
}

internal class WebSocketRepositoryFactory @Inject internal constructor(
    private val coreFactory: WebSocketCoreFactory,
    // Use a Provider to avoid a dependency circle since serverManager needs WebSocketCoreFactory
    private val serverManagerProvider: Provider<ServerManager>,
) {

    fun create(serverId: Int): WebSocketRepository {
        return WebSocketRepositoryImpl(coreFactory.create(serverId), serverManagerProvider.get())
    }
}
