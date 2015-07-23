require 'vertx-unit/test_suite'

def vertx_start_async start_future
  suite = VertxUnit::TestSuite.create('my_suite').test 'timer_test' do |context|
    async = context.async
    $vertx.set_timer 50 do
      context.fail Exception.new('the_failure')
    end
  end
  suite.run.resolve start_future
end