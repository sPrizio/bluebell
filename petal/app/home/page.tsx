import SimpleHero from "@/components/hero/SimpleHero";
import SimpleCard from "@/components/cards/SimpleCard";
import Footer from "@/components/footer/Footer";
import SimpleSection from "@/components/section/SimpleSection";
import ImageSection from "@/components/section/ImageSection";
import FeatureSection from "@/components/section/FeatureSection";

import {
  IconAdjustmentsBolt,
  IconBrandFacebook,
  IconBrandInstagram,
  IconBrandLinkedin,
  IconBrandX,
  IconCloud,
  IconCurrencyDollar,
  IconEaseInOut,
  IconGrowth,
  IconHeart,
  IconHelp,
  IconMail,
  IconMapPin,
  IconPhone,
  IconPlant,
  IconRouteAltLeft,
  IconSeeding,
  IconTerminal2,
} from "@tabler/icons-react";
import InfiniteScrollingCards from "@/components/cards/InfiniteScrollingCards";
import HighlightSection from "@/components/section/HighlightSection";
import ContactForm from "@/components/forms/ContactForm";
import ContactItem from "@/components/content/contact/ContactItem";
import ContactSocial from "@/components/content/contact/ContactSocial";
import {Separator} from "@/components/ui/separator";
import BackToTopButton from "@/components/buttons/BackToTopButton";
import AdminBar from "@/components/navigation/AdminBar";

/**
 * The home page
 *
 * @author Stephen Prizio
 * @version 0.0.1
 */
export default function HomePage() {

  const features: Array<FeatureType> = [
    {
      title: "Built for developers",
      description:
        "Built for engineers, developers, dreamers, thinkers and doers.",
      icon: <IconTerminal2/>,
      index: 0
    },
    {
      title: "Ease of use",
      description:
        "It's as easy as using an Apple, and as expensive as buying one.",
      icon: <IconEaseInOut/>,
      index: 1
    },
    {
      title: "Pricing like no other",
      description:
        "Our prices are best in the market. No cap, no lock, no credit card required.",
      icon: <IconCurrencyDollar/>,
      index: 2
    },
    {
      title: "100% Uptime guarantee",
      description: "We just cannot be taken down by anyone.",
      icon: <IconCloud/>,
      index: 3
    },
    {
      title: "Multi-tenant Architecture",
      description: "You can simply share passwords instead of buying new seats",
      icon: <IconRouteAltLeft/>,
      index: 4
    },
    {
      title: "24/7 Customer Support",
      description:
        "We are available a 100% of the time. Atleast our AI Agents are.",
      icon: <IconHelp/>,
      index: 5
    },
    {
      title: "Money back guarantee",
      description:
        "If you don't like EveryAI, we will convince you to like us.",
      icon: <IconAdjustmentsBolt/>,
      index: 6
    },
    {
      title: "And everything else",
      description: "I just ran out of copy ideas. Accept my sincere apologies",
      icon: <IconHeart/>,
      index: 7
    },
  ]

  const testimonials: Array<TestimonialType> = [
    {
      quote:
        "It was the best of times, it was the worst of times, it was the age of wisdom, it was the age of foolishness, it was the epoch of belief, it was the epoch of incredulity, it was the season of Light, it was the season of Darkness, it was the spring of hope, it was the winter of despair.",
      name: "Charles Dickens",
      title: "A Tale of Two Cities",
    },
    {
      quote:
        "To be, or not to be, that is the question: Whether 'tis nobler in the mind to suffer The slings and arrows of outrageous fortune, Or to take Arms against a Sea of troubles, And by opposing end them: to die, to sleep.",
      name: "William Shakespeare",
      title: "Hamlet",
    },
    {
      quote: "All that we see or seem is but a dream within a dream.",
      name: "Edgar Allan Poe",
      title: "A Dream Within a Dream",
    },
    {
      quote:
        "It is a truth universally acknowledged, that a single man in possession of a good fortune, must be in want of a wife.",
      name: "Jane Austen",
      title: "Pride and Prejudice",
    },
    {
      quote:
        "Call me Ishmael. Some years ago—never mind how long precisely—having little or no money in my purse, and nothing particular to interest me on shore, I thought I would sail about a little and see the watery part of the world.",
      name: "Herman Melville",
      title: "Moby-Dick",
    },
  ];

  const cards: Array<HighlightCardType> = [
    {
      description: "Lana Del Rey",
      title: "Summertime Sadness",
      src: "https://assets.aceternity.com/demos/lana-del-rey.jpeg",
      ctaText: "Visit",
      ctaLink: "https://ui.aceternity.com/templates",
      content:
        <p>
          Lana Del Rey, an iconic American singer-songwriter, is celebrated for
          her melancholic and cinematic music style. Born Elizabeth Woolridge
          Grant in New York City, she has captivated audiences worldwide with
          her haunting voice and introspective lyrics. <br/> <br/> Her songs
          often explore themes of tragic romance, glamour, and melancholia,
          drawing inspiration from both contemporary and vintage pop culture.
          With a career that has seen numerous critically acclaimed albums, Lana
          Del Rey has established herself as a unique and influential figure in
          the music industry, earning a dedicated fan base and numerous
          accolades.
        </p>
    },
    {
      description: "Babbu Maan",
      title: "Mitran Di Chhatri",
      src: "https://assets.aceternity.com/demos/babbu-maan.jpeg",
      ctaText: "Visit",
      ctaLink: "https://ui.aceternity.com/templates",
      content:
        <p>
          Babu Maan, a legendary Punjabi singer, is renowned for his soulful
          voice and profound lyrics that resonate deeply with his audience. Born
          in the village of Khant Maanpur in Punjab, India, he has become a
          cultural icon in the Punjabi music industry. <br/> <br/> His songs
          often reflect the struggles and triumphs of everyday life, capturing
          the essence of Punjabi culture and traditions. With a career spanning
          over two decades, Babu Maan has released numerous hit albums and
          singles that have garnered him a massive fan following both in India
          and abroad.
        </p>
    },

    {
      description: "Metallica",
      title: "For Whom The Bell Tolls",
      src: "https://assets.aceternity.com/demos/metallica.jpeg",
      ctaText: "Visit",
      ctaLink: "https://ui.aceternity.com/templates",
      content:
        <p>
          Metallica, an iconic American heavy metal band, is renowned for their
          powerful sound and intense performances that resonate deeply with
          their audience. Formed in Los Angeles, California, they have become a
          cultural icon in the heavy metal music industry. <br/> <br/> Their
          songs often reflect themes of aggression, social issues, and personal
          struggles, capturing the essence of the heavy metal genre. With a
          career spanning over four decades, Metallica has released numerous hit
          albums and singles that have garnered them a massive fan following
          both in the United States and abroad.
        </p>
    },
    {
      description: "Lord Himesh",
      title: "Aap Ka Suroor",
      src: "https://assets.aceternity.com/demos/aap-ka-suroor.jpeg",
      ctaText: "Visit",
      ctaLink: "https://ui.aceternity.com/templates",
      content:
        <p>
          Himesh Reshammiya, a renowned Indian music composer, singer, and
          actor, is celebrated for his distinctive voice and innovative
          compositions. Born in Mumbai, India, he has become a prominent figure
          in the Bollywood music industry. <br/> <br/> His songs often feature
          a blend of contemporary and traditional Indian music, capturing the
          essence of modern Bollywood soundtracks. With a career spanning over
          two decades, Himesh Reshammiya has released numerous hit albums and
          singles that have garnered him a massive fan following both in India
          and abroad.
        </p>
    },
  ];

  //  RENDER

  return (
    <div className={""}>
      <AdminBar variant={'tertiary'} />
      <SimpleHero
        variant={'image'}
        title={'Plant. Grow. Flourish.'}
        subtitle={'Money can be complicated and everyone seems to have the only advice you\'ll ever need to hear. We get it, we\'ve been there. ' +
          'Find out how simple things can be and empower your growth. Let your money work for you, so you can work on finding the next best ramen spot.'}
        size={'medium'}
        position={"left"}
        alignment={"left"}
        highlight={true}
        hasNavBar={true}
      />
      <SimpleSection
        title={'Money doesn\'t need to be complicated.'}
        subtitle={'Understanding finances isn\'t as difficult as you think. You\'re probably already on the right track.'}
        content={
          <div className="flex items-stretch flex-col lg:flex-row">
            <div className="basis-full lg:basis-1/3 mb-6 lg:mb-0 lg:mr-6">
              <SimpleCard
                title={'Plant'}
                subtitle={'Understand the situation. Define your goals.'}
                content={'We take a good, hard look at everything you own and owe. We start developing the outline and plan to set up your path to success. You\'ll be surprised at how well you\'re doing and close you actually are to your goals.'}
                icon={
                  <div className="rounded-full">
                    <div className="text-primary">
                      <IconSeeding size={40} stroke={1.5}/>
                    </div>
                  </div>
                }
                animated={true}
                delay={1}
              />
            </div>
            <div className="basis-full lg:basis-1/3 mb-6 lg:mb-0 lg:mr-6">
              <SimpleCard
                title={'Grow'}
                subtitle={'Build good habits. Reinforce responsibility.'}
                content={'Collaborate with a dedicated advisor to establish your growth strategy. Budget spending, Allocate income to invest, Consolidate debt. Build your future one step at a time. We\'ll be there every step of the way.'}
                icon={
                  <div className="rounded-full">
                    <div className="text-primary">
                      <IconGrowth size={40} stroke={1.5}/>
                    </div>
                  </div>
                }
                animated={true}
                delay={2}
              />
            </div>
            <div className="basis-full lg:basis-1/3">
              <SimpleCard
                title={'Flourish'}
                subtitle={'Enjoy the results.'}
                content={'Reap the benefits of your diligent planning. Watch your wealth grow with a few simple habits. Leverage your new skills to further develop your financial profile. Enjoy your success!'}
                icon={
                  <div className="rounded-full">
                    <div className="text-primary">
                      <IconPlant size={40} stroke={1.5}/>
                    </div>
                  </div>
                }
                animated={true}
                delay={3}
              />
            </div>
          </div>
        }
      />
      <ImageSection
        title={'Who We Are.'}
        subtitle={'Where we\'ve been and where we\'re going'}
        content={
          <div>
            We are Finance Corp, We provide Finance consulting from 30 years.<br/><br/>
            Accumsan est in tempus etos ullamcorper sem quam suscipit lacus maecenas tortor. Suspendisse gravida
            ornare
            non mattis velit rutrum modest sed do eiusmod tempor incididunt ut labore et dolore.<br/><br/>
            We have one of the philo sophia nec mei maiorum appell antur. Orci varius natoque penatibus et magnis
            dis
            parturient montes, nascetur ridiculus mus egestas varius penatibus.
          </div>
        }
        className={'bg-gray-100'}
        image={'https://demo.themenio.com/finance/image/photo-home-a.jpg'}
        alignment={'left'}
      />
      <FeatureSection
        title={'What We Do.'}
        subtitle={'A quick look at what we can do for you and how we do it'}
        features={features}
      />
      <HighlightSection
        title={'Our Team.'}
        subtitle={'Meet the wonderful team that makes all of this possible!'}
        highlights={cards}
        variant={'secondary'}
      />
      <SimpleSection
        title={'How We Did.'}
        subtitle={'Some of what our clients had to say about us'}
        content={
          <div className="">
            <InfiniteScrollingCards
              testimonials={testimonials}
            />
          </div>
        }
        variant={'white'}
      />
      <SimpleSection
        title={'Get In Touch.'}
        subtitle={'Let\'s talk! We\'d love to know more about you and your goals'}
        variant={"custom"}
        className={"bg-gray-100"}
        content={
          <div className="flex items-center flex-col lg:flex-row">
            <div className="lg:mr-12 basis-full lg:basis-1/3 w-full order-last lg:order-first">
              <ContactItem icon={<IconMapPin/>} text={'12th Wall Street, NY United States'}/>
              <ContactItem icon={<IconPhone/>} text={'+1 (514) 941-1025'}/>
              <ContactItem icon={<IconMail/>} text={'support@bluebell.com'}/>
              <div className="mt-12">Follow us on our socials!</div>
              <div className="mt-2">
                <div className="flex items-center text-primary">
                  <ContactSocial icon={<IconBrandFacebook size={30}/>} url={'#'}/>
                  <ContactSocial icon={<IconBrandLinkedin size={30}/>} url={'#'}/>
                  <ContactSocial icon={<IconBrandX size={30}/>} url={'#'}/>
                  <ContactSocial icon={<IconBrandInstagram size={30}/>} url={'#'}/>
                </div>
              </div>
            </div>
            <div className="lg:flex-grow w-full ">
              <ContactForm/>
            </div>
            <div className={"w-full my-10 lg:hidden"}>
              <Separator className={'bg-gray-300'}/>
            </div>
          </div>
        }
      />
      <Footer variant={"tertiary"}/>
      <BackToTopButton />
    </div>
  )
}