# meredith
A ChatOps FrontEnd

Meredith is a ChatOps FrontEnd.  It is designed to make processing of specific types of messages as painless as possible. Meredith is based on the Vert.X async framework and essentially all elements of the system are implemented as Verticles.

Inbound messages are received by the Relay, parsed by the Processor and, if necessary, sent to some back-end system for processing.

Outbound messages, including responses to previous messages as well as notifications and the like, are passed to the Relay.

There are a number of specific parts to a running Meredith instance, and some currently must be started in a specific order.

## Relay
The first point of contact for the chat system is the Relay.  A Relay is just that: a mechanism for passing chat-implementation-specific messages into Meredith and sending Meredith-generated replies back to the chat implementation.  Currently, only Slack is implemented as a Relay.  There should be, at most, a single Verticle implementation instance of a given chat-system.  This does mean that the Relay is the first single-point-of-failure within the system.  However, since the Relay's job is extremely simple (process inbound and outbound messages), that should currently pose little threat.  In this implementation, the Relay communicates _only_ with the Processor, and only to ingest chat-specific messages and emit Meredith-specific responses. 
// TODO Figure out if and how we can scale the Relay.  

## Processor 
The central figure in the architecture is the Processor.  The Processor subscribes to a specific topic on the Vert.X EventBus for inbound messages, ostensibly passed along via a Relay, and places outbound responses on a different topic.  This is where our system starts to look like a stored-state engine (which it is), much like a web-application but with a different form of cookie.  In order to make it possible to scale the Processor, all state for a given message must either be passed within that message or stored in the Brain.  The decision to pass or store is, obviously, dependent on the implementation of what state is required, which is dependent on the task that the target message is meant to effect.

## Brain
The Brain is the repository of system-level state.  Each item placed into the Brain has a non-infinite expiration-time on it.  The current implementation of the Brain is Redis.  

## Negotiator
A Negotiator is a language processor.  It is meant to receive a message that _appears_ to be a request for some sort of service and determines if some sort of conversational repair needs to be effected in order to make a real request.  Once a Negotiator is assigned to a given conversation, that Negotiator (by type, of course) is entirely responsible for handling that conversation to completion.  Since the definition of "Complete" can be broad, the definition here does **not** include asynchronous responses.

Currently, Negotiators are very simplistic and map to RegEx Pattern instances, which they use to match user messages to complete requests.  

## Savant
A Negotiator that is a sub-part of the Brain is the Savant.  A Savant is a data set that, externally, appears to dynamically acquired.  The actual implementations of this are obviously widely variable.  Savants are capable of answering very specific sorts of questions, and may obviously use other Savants, the rest of the Brain, or any other externally required data source to answer those question.  For practical purposes, a Savant responds to whatever query it handles and has Negotiator-like capabilities to deliver that information back to the requester.  

An example of a Savant might be an AWSAccountSavant, which is capable of querying your AWS account and collecting various types of data about what it finds there.  The Savant might respond to a number of different queries, where its Negotiator elements will attempt to determine what the request is and respond with the specific elements of the request.  Something like "How many r3.8xlarge instances are we running?" or "What is the VPCId of the current production system?" or "Describe the current production deployment".  Savants address "Wh-questions", which generally have direct and specific answers.  

## Interrogator
An Interrogator is a language processor, but one that initiates conversations instead of responding to them.  Almost all Interrogators must also be Negotiators or have access to one, since there is the very real possibility that the initial inquiry made by the Interrogator will need or prompt additional data/action.  Once an Interrogator has initiated a conversation, that conversation will require Negotiators to handle any conversational fallout (i.e. continued request/response activity).

Interrogators are not meant to act as Negotiators, although a given Interrogator might _also_ be a Negotiator (for its own or other conversations). 

An example Interrogator might be one that informs a user that a given monitoring event has occurred, like out of disk space or that a system has become unresponsive.  If the user needs to ask more information about that system, such conversations are handled by the Negotiator code, not the Interrogator.

## Broker
The final aspect of the Meredith system is the Broker.  A Broker's job is to communicate with any Automation Framework that is implemented as part of a ChatOps system.  A Broker works very much like a Relay in that its whole purpose is to transform messages from one form (Meredith messages) to another (Automation Framework messages).  The reason that there is a specific funnel for sending these messages to the Automation Framework is to facilitate separation between the language-management aspects of the ChatOps framework, which is handled by Meredith, and the "heavy-lifting" aspects which are handled by the Automation Framework.

Currently, Meredith has an adapter for the Semiote automation framework.  

# So What's REALLY Going On Here?
At its core, Meredith is meant to be paired with an automation framework that can execute tasks that might need to be done.  This might be "release project Z" or "deploy version X of this code to locationy Y".  It also migth be more simple things "pug me" or unusual things like "tell me how long the system has been alive" or "is @mykelalvis awake?"  Meredith is the system that is meant to process a cogent request to the automation framework.  What sorts of requests are valid are entirely dependent on the framework and how it is implemented.  However, Meredith is **not** the automation framework and should not be used as such.  Meredith is the go-between for the user and the automation framework.  This allows the automation framework to do its job independent of the ambiguities of human communication.  It allows the automation to live inside a well-defined sandbox of possible requests.  

## Requests
This is classic chat-bot territory, of which Hubot seems to be the most common.  This is a command to a listener to execute some action.

## Responses to Requests
This is what the system tells the user based on their request.  This is also squarely in ChatBot/Hubot territory, but is likely to be simpler within Meredith because of the more effective threading model that Vert.X gives over Node.js.  

## Unprompted Notifications
These are handled by the Interrogators, but the mechanism for noting that an Interrogation should be initiated may and likely will come from outside of Meredith.  This might be implemented as some sort of log watcher within Meredith, but would more likely be an emitted message from the Automation Framework.

## Basic Queries
These are data that could be supplied by the system, and can be implemented a number of ways.  The most common way is via the Savant, which is meant to respond to simple questions with precise answers.

## Simple Automated Tasks
These are tasks that require no additional input and should be handled by the automation framework.  The framework can give data to the system in the form of log watching or events.

## Complex or Orchestrated Tasks
These are tasks that are very complex or require multiple forms of orchestration to execute.  This is essentially launching a workflow within the automation framework that can be long-running and require multiple interactions with other actors within the system, such as users or other automation tasks.  This is entirely in the realm of the automation framework and should not be attempted using the basic tools within Meredith.





# The Automation Framework

There's been a lot of discussion about this so-called automation framework, so let's define what we mean.  An automation framework is any code that executes any well-defined process that is capable of speaking to Meredith using a Broker. It doesn't need to be a Vert.X application.  It doesn't even need to be truly asynchronous, although its Broker does.  An automation framework defines a set of possible actions and the required inputs needed and outputs produced.  

Meredith is designed to work natively with the Semiote framework.  Since Semiote is also a Vert.X application, the communication channels basically come pre-defined.  Also, since Semiote was built from the ground-up with Meredith in mind, it will generally be the standard by which Meredith deployments are measured.

However, what if you have a large investment in uDeploy, or GoCD?  You can write a Broker that can communicate with these systems and handle that interaction with Meredith.  Obviously, that Broker will be arbitrarily complex based on what sort of work your automation framework performs, but the magic of open-source and generic mechanisms for execution make this entirely possible.  We welcome your input in this area.

