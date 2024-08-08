import SimpleHero from "@/components/hero/SimpleHero";
import SimpleCard from "@/components/cards/SimpleCard";
import Footer from "@/components/footer/Footer";
import plant from '@/assets/images/hand-with-growing-plant.png';
import grow from '@/assets/images/reuse.png';
import flourish from '@/assets/images/flowers.png';
import SimpleSection from "@/components/section/SimpleSection";
import ImageSection from "@/components/section/ImageSection";
import FeatureSection from "@/components/section/FeatureSection";

import {
  IconAdjustmentsBolt,
  IconCloud,
  IconCurrencyDollar,
  IconEaseInOut, IconGrowth,
  IconHeart,
  IconHelp, IconPlant,
  IconRouteAltLeft, IconSeeding,
  IconTerminal2,
} from "@tabler/icons-react";

export default function HomePage() {

  const features : Array<FeatureType> = [
    {
      title: "Built for developers",
      description:
        "Built for engineers, developers, dreamers, thinkers and doers.",
      icon: <IconTerminal2 />,
      index: 0
    },
    {
      title: "Ease of use",
      description:
        "It's as easy as using an Apple, and as expensive as buying one.",
      icon: <IconEaseInOut />,
      index: 1
    },
    {
      title: "Pricing like no other",
      description:
        "Our prices are best in the market. No cap, no lock, no credit card required.",
      icon: <IconCurrencyDollar />,
      index: 2
    },
    {
      title: "100% Uptime guarantee",
      description: "We just cannot be taken down by anyone.",
      icon: <IconCloud />,
      index: 3
    },
    {
      title: "Multi-tenant Architecture",
      description: "You can simply share passwords instead of buying new seats",
      icon: <IconRouteAltLeft />,
      index: 4
    },
    {
      title: "24/7 Customer Support",
      description:
        "We are available a 100% of the time. Atleast our AI Agents are.",
      icon: <IconHelp />,
      index: 5
    },
    {
      title: "Money back guarantee",
      description:
        "If you don't like EveryAI, we will convince you to like us.",
      icon: <IconAdjustmentsBolt />,
      index: 6
    },
    {
      title: "And everything else",
      description: "I just ran out of copy ideas. Accept my sincere apologies",
      icon: <IconHeart />,
      index: 7
    },
  ]


  //  RENDER

  return (
    <div className={""}>
      {/*<NavBar variant={'primary'} />
      <NavBar variant={'secondary'} />
      <NavBar variant={'tertiary'} />
      <NavBar variant={'white'} />*/}
      {/*<SimpleHero
        variant={'primary'}
        title={'Plant. Grow. Flourish.'}
        subtitle={'Cryptro is a decentralised public blockchain and cryptocurrency project and is fully open source. Cryptro is developing a smart contract platform'}
        size={'small'}
        alignment={'left'}
        position={'left'}
        hasNavBar={true}
        highlight={true}
      />
      <SimpleHero
        variant={'secondary'}
        title={'Plant. Grow. Flourish.'}
        subtitle={'Cryptro is a decentralised public blockchain and cryptocurrency project and is fully open source. Cryptro is developing a smart contract platform'}
        size={'medium'}
        alignment={'center'}
        position={'center'}
        hasNavBar={true}
        highlight={true}
      />
      <SimpleHero
        variant={'tertiary'}
        title={'Plant. Grow. Flourish.'}
        subtitle={'Cryptro is a decentralised public blockchain and cryptocurrency project and is fully open source. Cryptro is developing a smart contract platform'}
        size={'large'}
        alignment={'right'}
        position={'right'}
        highlight={true}
      />
      <SimpleHero
        variant={'white'}
        title={'Plant. Grow. Flourish.'}
        subtitle={'Cryptro is a decentralised public blockchain and cryptocurrency project and is fully open source. Cryptro is developing a smart contract platform'}
        size={'full'}
        position={"center"}
        highlight={true}
      />*/}
      <SimpleHero
        variant={'image'}
        title={'Plant. Grow. Flourish.'}
        subtitle={'Finances can be messy, money can be complicated and everyone seems to have the only advice you\'ll ever need to hear. We get it, we\'ve been there. ' +
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
          <div className="flex items-stretch">
            <div className="basis-1/3 mr-6">
              <SimpleCard
                title={'Plant'}
                subtitle={'Understand the situation. Define your goals.'}
                content={'We take a good, hard look at everything you own and owe. We start developing the outline and plan to set up your path to success. You\'ll be surprised at how well you\'re doing and close you actually are to your goals.'}
                icon={
                  <div className="rounded-full">
                    <div className="text-primary">
                      <IconSeeding size={40} stroke={1.5} />
                    </div>
                  </div>
                }
                animated={true}
                delay={1}
              />
            </div>
            <div className="basis-1/3 mr-6">
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
            <div className="basis-1/3">
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
        title={'About Us.'}
        subtitle={'Who We Are and What We\'re All About'}
        content={<></>}
        className={'bg-gray-100'}
        image={'https://demo.themenio.com/finance/image/photo-home-a.jpg'}
        alignment={'left'}
      />
      <FeatureSection
        title={'What We Do.'}
        subtitle={'A quick look at what we can do for you and how we do it'}
        features={features}
      />
      <Footer variant={"secondary"}/>
    </div>
  )
}