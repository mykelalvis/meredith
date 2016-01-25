# Description:
#   Pass all 
#
# Notes:
#   This listener passes all messages from name or alias along to meredith-clearinghouse

config = {
	"name" : "${project.artifactId}"
}
options = {
	"workerPoolSize":10
	"eventBusURL" : 'http://localhost:8080/eventbus/'
}


headers = {} 

EventBus = require 'vertx3-eventbus-client'
eb = new EventBus 'http://localhost:8080/eventbus/'
eb.onopen = () -> 
	eb.registerHandler "meredith.response", headers, (err,res) ->
	# Figure out what I'm responding to
    if err
      console.error(err);
      return
    console.log JSON.stringify(res)
	
# TODO Put an id in to this instance of meredith and only respond to that id
#  i.e. 
# meredithId = "meredith.response." + uuid 
# eb.registerHander meredithId, headers () ->

	
module.exports = (robot) ->
	robot.respond /(.*)/i, (msg) ->
		x = eb
		send_it_along(robot,msg)

send_it_along = (robot,res) ->
	res.send "res == " + res.message.text

