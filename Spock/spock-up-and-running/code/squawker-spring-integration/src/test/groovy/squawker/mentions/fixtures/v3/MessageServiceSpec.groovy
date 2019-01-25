package squawker.mentions.fixtures.v3

import com.oreilly.spock.TruncateTables
import org.skife.jdbi.v2.DBI
import org.skife.jdbi.v2.tweak.HandleCallback
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.jdbc.Sql
import spock.lang.Specification
import squawker.jdbi.UserStore
import squawker.mentions.DBIConnector
import squawker.mentions.MentionStore
import squawker.mentions.MessageService
import squawker.mentions.TableHelper
import squawker.spring.Main

// tag::class-level-fixture[]
@SpringBootTest(classes = Main)
@Sql("/fixtures/users.sql")
class MessageServiceSpec extends Specification
// end::class-level-fixture[]
  implements TableHelper {

  @Autowired @TruncateTables(DBIConnector) DBI dbi
  @Autowired MessageService messageService
  @Autowired UserStore userStore
  @Autowired MentionStore mentionStore

  def "mentions are persisted with message"() {
    given:
    def user = userStore.find(username)

    when:
    def message = messageService.postMessage(user, messageText)

    then:
    count("mention") == mentionedUsernames.size()
    mentionedUsernames.every {
      mentionStore.mentionsOf(userStore.find(it)) == [message]
    }

    where:
    username = "kirk"
    mentionedUsernames = ["spock", "bones"]
    messageText = "@${mentionedUsernames[0]}, @${mentionedUsernames[1]}" +
      " meet me in the transporter room!"
  }

  @Override
  <R> R withHandle(HandleCallback<R> closure) {
    dbi.withHandle(closure)
  }
}
