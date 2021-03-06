/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.wicketstuff.push;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * @author <a href="http://sebthom.de/">Sebastian Thomschke</a>
 */
public abstract class AbstractPushService implements IPushService
{
	protected final ConcurrentHashMap<IPushChannel<?>, CopyOnWriteArraySet<IPushNode<?>>> nodesByChannels = new ConcurrentHashMap<IPushChannel<?>, CopyOnWriteArraySet<IPushNode<?>>>();

	/**
	 * {@inheritDoc}
	 */
	public <EventType> void connectToChannel(final IPushNode<EventType> node,
		final IPushChannel<EventType> channel)
	{
		if (node == null)
			throw new IllegalArgumentException("Argument [node] must not be null");
		if (channel == null)
			throw new IllegalArgumentException("Argument [channel] must not be null");

		final Set<IPushNode<?>> pnodes = nodesByChannels.get(channel);
		if (pnodes == null)
			throw new IllegalArgumentException("Unknown channel " + channel);

		pnodes.add(node);
	}

	/**
	 * {@inheritDoc}
	 */
	public <EventType> IPushChannel<EventType> createChannel(final String label)
	{
		final PushChannel<EventType> channel = new PushChannel<EventType>(label);
		nodesByChannels.put(channel, new CopyOnWriteArraySet<IPushNode<?>>());
		return channel;
	}

	protected void disconnectFromAllChannels(final IPushNode<?> node)
	{
		for (final Set<IPushNode<?>> pnodes : nodesByChannels.values())
			pnodes.remove(node);
	}

	/**
	 * {@inheritDoc}
	 */
	public <EventType> void disconnectFromChannel(final IPushNode<EventType> node,
		final IPushChannel<EventType> channel)
	{
		if (node == null)
			throw new IllegalArgumentException("Argument [node] must not be null");
		if (channel == null)
			throw new IllegalArgumentException("Argument [channel] must not be null");

		final Set<IPushNode<?>> pnodes = nodesByChannels.get(channel);
		if (pnodes == null)
			throw new IllegalArgumentException("Unknown channel " + channel);

		pnodes.remove(node);
	}

	/**
	 * {@inheritDoc}
	 */
	public <EventType> void removeChannel(final IPushChannel<EventType> channel)
	{
		if (channel == null)
			throw new IllegalArgumentException("Argument [channel] must not be null");

		nodesByChannels.remove(channel);
	}
}
